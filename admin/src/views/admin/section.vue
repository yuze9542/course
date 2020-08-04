<template>
  <div>

    <h3>{{chapter.name}}</h3>
    <hr/>
    <pagination ref="pagination" v-bind:list="list"></pagination>
    <p></p>
    <table id="simple-table" class="table  table-bordered table-hover">
      <thead>
      <tr>
        <th>id</th>
        <th>标题</th>
        <th>课程</th>
        <th>大章</th>
        <th>视频</th>
        <th>时长</th>
        <th>收费</th>
        <th>顺序</th>
        <th>修改时间</th>
        <th>vod</th>
      <th>操作</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="section in sections">
        <td>{{section.id}}</td>
        <td>{{section.title}}</td>
        <td>{{section.courseId}}</td>
        <td>{{section.chapterId}}</td>
        <td>{{section.video}}</td>
        <td>{{section.time  | formatSecond}}</td>
<!--        <td>{{section.charge}}</td>-->
        <td>{{SECTION_CHARGE | optionKV(section.charge)}}</td>
        <td>{{section.sort}}</td>
        <td>{{section.updatedAt}}</td>
        <td>{{section.vod}}</td>
      <td>
        <div class="hidden-sm hidden-xs btn-group">
          <button @click="edit(section)" class="btn btn-xs btn-info">
            <i class="ace-icon fa fa-pencil bigger-120"></i>
          </button>

          <button @click="del(section.id)" class="btn btn-xs btn-danger">
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
                <a href="#" @click="edit(section)" class="tooltip-success" data-rel="tooltip" title="Edit">
																			<span class="green">
																				<i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
																			</span>
                </a>
              </li>

              <li>
                <a href="#" @click="del(section.id)" class="tooltip-error" data-rel="tooltip" title="Delete">
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
      <router-link to="/business/chapter" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-arrow-left red3"></i>
        返回大章
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
                <label class="col-sm-2 control-label">标题</label>
                <div class="col-sm-10">
                  <input v-model="section.title" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">课程</label>
                <div class="col-sm-10">
                  <p class="form-control-static">{{course.id}}</p>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">大章</label>
                <div class="col-sm-10">
                  <p class="form-control-static">{{chapter.id}}</p>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">视频</label>
                <div class="col-sm-10">
                  <big-file
                    :input-id="'video-upload'"
                    :suffixes="['mp4']"
                    :text="'上传大视频'"
                    :use="FILE_USE.COURSE.key"
                    :after-upload="afterUpdate"></big-file>
                  <div class="row" v-show="section.video">
                    <div class="col-md-10">
                      <video id="video" :src="section.video" alt="" controls="controls">
                      </video>
                    </div>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">时长</label>
                <div class="col-sm-10">
                  <input v-model="section.time" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">收费</label>
                <div class="col-sm-10">
                  <select v-model="section.charge"  class="form-control">
                    <option v-for="o in SECTION_CHARGE" v-bind:value="o.key">{{o.value}}</option>
                  </select>
<!--                  <input v-model="section.charge" class="form-control">-->
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">顺序</label>
                <div class="col-sm-10">
                  <input v-model="section.sort" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">vod</label>
                <div class="col-sm-10">
                  <input v-model="section.vod" class="form-control">
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
    import File from "../../components/uploadFile";
    import BigFile from "../../components/big-file";

    export default {
        name: 'business-section',
        components: {Pagination,File,BigFile},
        data: function () {
            return {
                //section绑定表单数据
                section: {},
                sections: [],
                FILE_USE:FILE_USE,
                SECTION_CHARGE: SECTION_CHARGE,
                course:{},
                chapter:{},
            }
        },
        mounted: function () {
            this.$parent.activeSidebar("business-course-sidebar")
            let _this = this;

            let course = SessionStorage.get(SESSION_KEY_COURSE) ||  {} // 加{} 防止course为空报错
            let chapter = SessionStorage.get(SESSION_KEY_CHAPTER) ||  {} // 加{} 防止chapter为空报错
            console.log("course",course)
            console.log("chapter",chapter)
            if(Tool.isEmpty(course) || Tool.isEmpty(chapter)){
                _this.$router.push("./welcome")
            }
            _this.course = course
            _this.chapter = chapter
            _this.list(1);
        },
        methods: {
            del(id){
                let _this = this;
                Confirm.show("删掉可就没了",function () {
                    //删除方法
                    Loading.show()
                    _this.$ajax.delete(process.env.VUE_APP_SERVER+"/business/admin/section/delete/"+id).then((response) => {
                        Loading.hide()
                        console.log("删除小节列表:", response);
                        let resp = response.data;
                        if(resp.success){
                            _this.list(1);
                            Toast.success("删除成功")
                        }
                    })
                })



            },
            edit(section){
                let _this = this;
                _this.section = $.extend({},section);
                $("#form-modal").modal("show")
            },
            list(page) {
                let _this = this;
                Loading.show();
                //得到数据
                _this.$ajax.post(process.env.VUE_APP_SERVER+"/business/admin/section/list", {
                    page: page,
                    size: _this.$refs.pagination.size,
                    courseId: _this.course.id,
                    chapterId: _this.chapter.id,
                }).then((response) => {
                    Loading.hide();
                    let resp = response.data;
                    _this.sections = resp.content.list;
                    _this.$refs.pagination.render(page, resp.content.total)
                })
            },
            save(){
                let _this = this;

                // 保存校验
                if (1 != 1
                    || !Validator.require(_this.section.title, "标题")
                    || !Validator.length(_this.section.title, "标题", 1, 50)
                    || !Validator.length(_this.section.video, "视频", 1, 200)
                ) {
                    return;
                }

                _this.section.courseId = _this.course.id;
                _this.section.chapterId = _this.chapter.id;

                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER+"/business/admin/section/save",
                _this.section).then((response) => {
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
                _this.section = {},
                $("#form-modal").modal("show");
            },

            afterUpdate(resp){
                let _this = this;
                let video = resp.content.path;
                _this.section.video = video;
                _this.getTime()
            },
            //获取时长
            getTime() {
                let _this = this;
                setTimeout(function () {
                    let ele = $("#video");
                    _this.section.time = parseInt(ele.duration, 10);
                }, 1000);
            },
        }
    }
</script>
<style scoped>
  video{
    width:100%;
    height:auto;
    margin: 5px;
  }
</style>