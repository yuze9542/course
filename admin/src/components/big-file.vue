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
            /**
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
                let shardSize = 2 *1024*1024 ;  //以20MB为一个分片
                let shardIndex = 3 ;  //分片索引 1 表示第一个分片
                let start = (shardIndex - 1) * shardSize; //当前分片起始位置
                let end = Math.min(start + shardSize,file.size);//当前分片结束位置
                let fileShard = file.slice(start,end);

                let size = file.size;
                let shardTotal = Math.ceil(size / shardSize);   //分片总数


                //key: file必须和后端Controller参数名保持一致
                // formData.append("shard",fileShard);
                // formData.append("use",_this.use);
                // formData.append("shardIndex",shardIndex);
                // formData.append("shardSize",shardSize);
                // formData.append("size",size);
                // formData.append("shardTotal",shardTotal);
                // formData.append("suffix",suffix); //后缀
                // formData.append("name",name);
                // formData.append("key",key62);
                // Loading.show();
                // _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/upload', formData).then((response)=>{
                //     Loading.hide();
                //     let resp = response.data;
                //     console.log("上传文件成功: ",resp);
                //     _this.afterUpload(resp);
                //     $("#"+_this.inputId+"-input").val("");
                // })


                //将图片转为base64进行传输
                let fileReader = new FileReader();
                fileReader.onload = function (e) {
                    let base64 = e.target.result;
                    console.log("base64",base64);

                    let param = {
                        'shard':base64,
                        'shardIndex': shardIndex,
                        'shardSize': shardSize,
                        'shardTotal': shardTotal,
                        'use': _this.use,
                        'name': file.name,
                        'suffix': suffix,
                        'size': file.size,
                        'key': key62
                    };
                    _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/upload', param).then((response)=>{
                        Loading.hide();
                        let resp = response.data;
                        _this.afterUpload(resp);
                        $("#"+_this.inputId+"-input").val("");
                    })
                };
                fileReader.readAsDataURL(fileShard);
            }
        }

    }
</script>

