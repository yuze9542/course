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
        name: 'file',
        props:{
            inputId: {
                default: "file-upload",
            },
            text: {
                default: "文件上传",
            },
            suffixes: {
                default: "",
            },
            afterUpload:{
                type: Function,
                default: null
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
                //key: file必须和后端Controller参数名保持一致
                formData.append("file",file);
                console.log("SSSSS",formData);
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/upload', formData).then((response)=>{
                    Loading.hide();
                    let resp = response.data;
                    console.log("上传文件成功: ",resp);
                    _this.afterUpload(resp);
                    $("#"+_this.inputId+"-input").val("");
                })
            }
        }

    }
</script>

