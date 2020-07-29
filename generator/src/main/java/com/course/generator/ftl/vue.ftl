<template>
  <div>

    <pagination ref="pagination" v-bind:list="list"></pagination>
    <p></p>
    <table id="simple-table" class="table  table-bordered table-hover">
      <thead>
      <tr>
        <#list fieldList as field>
            <#if field.nameHump!="createdAt" && field.nameHump!="updateAt">
        <th>${field.nameCn}</th>
            </#if>
        </#list>
      <th>操作</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="${domain} in ${domain}s">
        <#list fieldList as field>
            <#if field.nameHump!="createdAt" && field.nameHump!="updateAt">
        <td>{{${domain}.${field.nameHump}}}</td>
            </#if>
        </#list>
      <td>
        <div class="hidden-sm hidden-xs btn-group">
          <button @click="edit(${domain})" class="btn btn-xs btn-info">
            <i class="ace-icon fa fa-pencil bigger-120"></i>
          </button>

          <button @click="del(${domain}.id)" class="btn btn-xs btn-danger">
            <i class="ace-icon fa fa-trash-o bigger-120"></i>
          </button>

        </div>
        <!--          小屏幕隐藏按钮-->
        <div class="hidden-md hidden-lg">
          <div class="inline pos-rel">
            <button class="btn btn-minier btn-primary dropdown-toggle" data-toggle="dropdown" data-position="auto">
              <i class="ace-icon fa fa-cog icon-only bigger-110"></i>
            </button>

            <ul
              class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">

              <li>
                <a href="#" @click="edit(${domain})" class="tooltip-success" data-rel="tooltip" title="Edit">
																			<span class="green">
																				<i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
																			</span>
                </a>
              </li>

              <li>
                <a href="#" @click="del(${domain}.id)" class="tooltip-error" data-rel="tooltip" title="Delete">
																			<span class="red">
																				<i class="ace-icon fa fa-trash-o bigger-120"></i>
																			</span>
                </a>
              </li>
            </ul>
          </div>
        </div>
      </td>
      </tr>

      </tbody>
    </table>
    <p>
      <button @click="add()" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-edit red3"></i>
        新增
      </button>
      &nbsp;
      <button @click="list(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh red3"></i>
        刷新
      </button>
    </p>
    <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
              aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">表单</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <#list fieldList as field>
                  <#if field.name!="id" && field.nameHump!="createdAt" && field.nameHump!="updatedAt">
              <div class="form-group">
                <label class="col-sm-2 control-label">${field.nameCn}</label>
                <div class="col-sm-10">
                  <input v-model="${domain}.${field.nameHump}" class="form-control">
                </div>
              </div>
                  </#if>
            </#list>
            </form>
          </div>
          <div class="modal-footer">
            <!--            data-dismiss="modal" 关闭css名称为modal的选择器
                            data-toggle="modal" 开启css名称为modal的选择器 -->
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            <button @click="save()" type="button" class="btn btn-primary">保存</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
  </div>

</template>
<script>
    import Pagination from "../../components/pagination";

    export default {
        name: '${module}-${domain}',
        components: {Pagination},
        data: function () {
            return {
                //${domain}绑定表单数据
                ${domain}: {},
                ${domain}s: []
            }
        },
        mounted: function () {
            // this.$parent.activeSidebar("${module}-${domain}-sidebar")
            let _this = this;
            _this.list(1);
        },
        methods: {
            del(id){
                let _this = this;
                Confirm.show("删掉可就没了",function () {
                    //删除方法
                    Loading.show()
                    _this.$ajax.delete(process.env.VUE_APP_SERVER+"/${module}/admin/${domain}/delete/"+id).then((response) => {
                        Loading.hide()
                        console.log("删除${tableNameCn}列表:", response);
                        let resp = response.data;
                        if(resp.success){
                            _this.list(1);
                            Toast.success("删除成功")
                        }
                    })
                })



            },
            edit(${domain}){
                let _this = this;
                _this.${domain} = $.extend({},${domain});
                $("#form-modal").modal("show")
            },
            list(page) {
                let _this = this;
                Loading.show();
                //得到数据
                _this.$ajax.post(process.env.VUE_APP_SERVER+"/${module}/admin/${domain}/list", {
                    page: page,
                    size: _this.$refs.pagination.size,
                }).then((response) => {
                    Loading.hide();
                    let resp = response.data;
                    _this.${domain}s = resp.content.list;
                    _this.$refs.pagination.render(page, resp.content.total)
                })
            },
            save(){
                let _this = this;

                // 保存校验
                if (1 != 1
                    <#list fieldList as field>
                    <#if field.name!="id" && field.nameHump!="createdAt" && field.nameHump!="updatedAt" && field.nameHump!="sort">
                    <#if !field.nullAble>
                    || !Validator.require(_this.${domain}.${field.nameHump}, "${field.nameCn}")
                    </#if>
                    <#if (field.length > 0)>
                    || !Validator.length(_this.${domain}.${field.nameHump}, "${field.nameCn}", 1, ${field.length?c})
                    </#if>
                    </#if>
                    </#list>
                ) {
                    return;
                }


                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER+"/${module}/admin/${domain}/save",
                _this.${domain}).then((response) => {
                    Loading.hide();
                    let resp = response.data; // 返回的是 ResponseDto 数据
                    if(resp.success){
                        $("#form-modal").modal("hide");
                        _this.list(1);
                        Toast.success("保存成功")
                    }else {
                        Toast.warning(resp.message)
                    }
                })
            },
            add(){
                let _this = this;
                _this.${domain} = {},
                $("#form-modal").modal("show");
            }
        }
    }
</script>