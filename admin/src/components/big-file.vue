<template>
  <div>
    <input class="hidden" ref="file"  :id="inputId+'-input'"  @change="uploadFile()" type="file" >
    <button type="button" @click="selectFile()" class="btn btn-white btn-default btn-round">
      <i class="ace-icon fa fa-upload"></i>
      {{text}}
    </button>
  </div>
</template>

<script>
    export default {
        name: 'big-file',
        props:{
            inputId: {
                default: "file-upload",
            },
            text: {
                default: "大文件上传",
            },
            suffixes: {
                default: "",
            },
            afterUpload:{
                type: Function,
                default: null
            },
            use:{
                default:"",
            }
        },
        methods:{
            selectFile(){
                let _this = this;
                $("#"+_this.inputId+"-input").trigger("click");
            },
            getFileShard (shardIndex, shardSize) {
                let _this = this;
                let file = _this.$refs.file.files[0];
                let start = (shardIndex - 1) * shardSize;	//当前分片起始位置
                let end = Math.min(file.size, start + shardSize); //当前分片结束位置
                let fileShard = file.slice(start, end); //从文件中截取当前的分片数据
                return fileShard;
            },
            /*
             * 上传文件
             */
            uploadFile(){
                let _this = this;
                //window.FormData 需要键值对形式
                let formData = new window.FormData;
                // file === document.querySelector("#file-upload-input").files[0]
                let file = _this.$refs.file.files[0]

                let key = hex_md5(file);
                let key10 = parseInt(key,16);
                let key62= Tool._10to62(key10);
                console.log(key,key10,key62);

                let suffixs = _this.suffixes;
                let fileName = file.name;
                let  suffix = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length).toLowerCase();
                let validateSuffix = false;
                for (let i = 0; i < suffixs.length; i++) {
                    if(suffixs[i].toLowerCase() === suffix){
                        validateSuffix = true;
                        break;
                    }
                }
                if(!validateSuffix){
                    Toast.warning("文件格式不支持 只支持"+ suffixs.join(","));
                    $("#"+_this.inputId+"-input").val("");
                    return;
                }

                //文件分片
                let shardSize = 4 *1024*1024 ;  //以20MB为一个分片
                let shardIndex = 1 ;  //分片索引 1 表示第一个分片

                let size = file.size;
                let shardTotal = Math.ceil(size / shardSize);   //分片总数
                let param = {
                    'shardIndex': shardIndex,
                    'shardSize': shardSize,
                    'shardTotal': shardTotal,
                    'use': _this.use,
                    'name': file.name,
                    'suffix': suffix,
                    'size': file.size,
                    'key': key62
                };
                _this.upload(param);
            },
            /**
             * 将分片数据转成base64进行上传
             */
            upload (param) {
                let _this = this;
                let shardIndex = param.shardIndex;
                let shardTotal = param.shardTotal;
                let shardSize = param.shardSize;
                let fileShard = _this.getFileShard(shardIndex, shardSize);
                // 将图片转为base64进行传输
                let fileReader = new FileReader();

                // Progress.show(parseInt((shardIndex - 1) * 100 / shardTotal));
                fileReader.onload = function (e) {
                    let base64 = e.target.result;
                    // console.log("base64:", base64);

                    param.shard = base64;

                    _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/upload', param).then((response) => {
                        let resp = response.data;
                        console.log("上传文件成功：", resp);
                        // Progress.show(parseInt(shardIndex * 100 / shardTotal));
                        if (shardIndex < shardTotal) {
                            // 上传下一个分片
                            param.shardIndex = param.shardIndex + 1;
                            _this.upload(param);
                        } else {
                            // Progress.hide();
                            _this.afterUpload(resp);
                            $("#" + _this.inputId + "-input").val("");
                        }
                    });
                };
                fileReader.readAsDataURL(fileShard);
            },
        }

    }
</script>

