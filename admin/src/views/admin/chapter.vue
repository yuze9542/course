<template>
  <div>
    <h3>{{course.name}}</h3>
    <hr/>
    <pagination ref="pagination" v-bind:list="list"></pagination>
    <p></p>
    <table id="simple-table" class="table  table-bordered table-hover">
      <thead>
      <tr>

        <th>ID</th>
        <th>名称</th>
        <th>课程ID</th>
        <th>操作</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="chapter in chapters">
        <td>{{chapter.id}}</td>
        <td>{{chapter.name}}</td>
        <td>{{chapter.courseId}}</td>
        <td>
          <div class="hidden-sm hidden-xs btn-group">
            <button @click="toSection(chapter)" class="btn btn-xs btn-info">
              小节
            </button>
            <button @click="edit(chapter)" class="btn btn-xs btn-info">
              <i class="ace-icon fa fa-pencil bigger-120"></i>
            </button>
            <button @click="del(chapter.id)" class="btn btn-xs btn-danger">
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
                  <a href="#" @click="edit(chapter)"   class="tooltip-success" data-rel="tooltip" title="Edit">
                    <span class="green">
                      <i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
                    </span>
                  </a>
                </li>

                <li>
                  <a href="#"  @click="del(chapter.id)" class="tooltip-error" data-rel="tooltip" title="Delete">
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
<!--      相当于a标签-->
      <router-link to="/business/course" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-arrow-left red3"></i>
        返回课程
      </router-link>

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
              <div class="form-group">
                <label class="col-sm-2 control-label">名称</label>
                <div class="col-sm-10">
                  <input v-model="chapter.name"   class="form-control"  placeholder="名称">
                </div>
              </div>
              <div class="form-group">
                <label  class="col-sm-2 control-label">课程ID</label>
                <div class="col-sm-10">
                  <p class="form-control-static">{{course.name}}</p>
                </div>
              </div>
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
        name: 'chapter',
        components: {Pagination},
        data: function () {
            return {
                //chapter绑定表单数据
                chapter: {},
                chapters: [],
                course: {}
            }
        },
        mounted: function () {
            this.$parent.activeSidebar("business-course-sidebar")
            let _this = this;
            let course = SessionStorage.get(SESSION_KEY_COURSE) ||  {} // 加{} 防止course为空报错
            if(Tool.isEmpty(course)){
                _this.$router.push("./welcome")
            }
            _this.course = course
            _this.list(1);

        },
        methods: {
            toSection(chapter){
                let _this = this;
                SessionStorage.set(SESSION_KEY_CHAPTER,chapter);
                _this.$router.push("/business/section");
            },
            del(id){
                let _this = this;
                Confirm.show("删掉可就没了",function () {
                    //删除方法
                    Loading.show()
                    _this.$ajax.delete(process.env.VUE_APP_SERVER+"/business/admin/chapter/delete/"+id).then((response) => {
                        Loading.hide()
                        console.log("删除大章列表:", response);
                        let resp = response.data;
                        if(resp.success){
                            _this.list(1);
                            Toast.success("删除成功")
                        }
                    })
                })



            },
            edit(chapter){
                let _this = this;
                _this.chapter = $.extend({},chapter);
                $("#form-modal").modal("show")
            },
            list(page) {
                let _this = this;
                Loading.show();
                //得到数据
                _this.$ajax.post(process.env.VUE_APP_SERVER+"/business/admin/chapter/list", {
                    page: page,
                    size: _this.$refs.pagination.size,
                    courseId: _this.course.id,
                }).then((response) => {
                    Loading.hide();
                    let resp = response.data;
                    _this.chapters = resp.content.list;
                    _this.$refs.pagination.render(page, resp.content.total)
                })
            },
            save(){
                let _this = this;
                _this.chapter.courseId = _this.course.id;
                if(!Validator.require(_this.chapter.name,"名称") ||
                    !Validator.length(_this.chapter.courseId,"课程ID",1,8) ){
                    return ;
                }


                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER+"/business/admin/chapter/save",
                _this.chapter).then((response) => {
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
                _this.chapter = {},
                $("#form-modal").modal("show");
            }
        }
    }
</script>