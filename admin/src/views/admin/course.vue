<template>
  <div>
    <p>
      <button v-on:click="add()" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-edit"></i>
        新增
      </button>
      &nbsp;
      <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh"></i>
        刷新
      </button>
    </p>

    <pagination ref="pagination" v-bind:list="list" v-bind:itemCount="8"></pagination>
    <div>
      <div v-for="course in courses" class="col-xs-6 col-sm-4 col-md-3">
        <div class="thumbnail search-thumbnail">
          <img v-show="!course.image" class="media-object" src="/static/image/demo-course.jpg" />
          <img v-show="course.image" class="media-object" :src="course.image" />
          <div class="caption">
            <div class="clearfix">
              <span class="pull-right label label-primary info-label">
                {{COURSE_CHARGE | optionKV(course.charge)}}
              </span>
              <span class="pull-right label label-primary info-label">
                {{COURSE_LEVEL | optionKV(course.level)}}
              </span>
              <span class="pull-right label label-primary info-label">
                {{COURSE_STATUS | optionKV(course.status)}}
              </span>
            </div>

            <h3 class="search-title">
              <a href="#" class="blue">{{course.name}}</a>
            </h3>
            <div v-for="teacher in teachers.filter(t=>{return t.id===course.teacherId})" class="profile-activity clearfix">
              <div>
                <img v-show="!teacher.image" class="pull-left" src="/ace/assets/images/avatars/avatar5.png">
                <img v-show="teacher.image" class="pull-left" v-bind:src="teacher.image">
                <a class="user" href="#"> {{teacher.name}} </a>
                <br>
                {{teacher.position}}
              </div>
            </div>
            <p>{{course.summary}}</p>
            <p>时长{{course.time | formatSecond}}</p>
            <p>
              <button v-on:click="edit(course)" class="btn btn-white btn-around btn-xs btn-info">
                编辑
              </button>&nbsp;
              <button v-on:click="toContent(course)" class="btn btn-white btn-around btn-xs btn-info">
                内容
              </button>&nbsp;
              <button v-on:click="openSortModal(course)" class="btn btn-white btn-around btn-xs btn-info">
                排序
              </button>&nbsp;
              <button v-on:click="del(course.id)" class="btn btn-white btn-around btn-xs btn-danger">
                删除
              </button>&nbsp;
              <button v-on:click="toChapter(course)" class="btn btn-white btn-around btn-xs btn-danger">
                大章
              </button>&nbsp;
            </p>

          </div>
        </div>
      </div>
    </div>


    <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">表单</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label class="col-sm-2 control-label">分类</label>
                <div class="col-sm-10">
                  <ul id="tree" class="ztree"></ul>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">名称</label>
                <div class="col-sm-10">
                  <input v-model="course.name" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">讲师</label>
                <div class="col-sm-10">
                  <select v-model="course.teacherId" class="form-control">
                    <option v-for="o in teachers" v-bind:value="o.id">{{o.name}}</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">概述</label>
                <div class="col-sm-10">
                  <input v-model="course.summary" class="form-control">
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">价格（元）</label>
                <div class="col-sm-10">
                  <input v-model="course.price" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">封面</label>
                <div class="col-sm-10">
                  <file
                    :input-id="'image-upload'"
                    :suffixes="['jpg','jpeg','png']"
                    :text="'封面上传'"
                    :use="FILE_USE.COURSE.key"
                    :after-upload="afterUpdate"></file>
                  <div class="row" v-show="course.image">
                    <div class="col-md-4">
                      <img :src="course.image" alt="" class="img-responsive">
                    </div>
                  </div>
<!--                  <input v-model="course.image" class="form-control">-->
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">级别</label>
                <div class="col-sm-10">
                  <select v-model="course.level" class="form-control">
                    <option v-for="o in COURSE_LEVEL" v-bind:value="o.key">{{o.value}}</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">收费</label>
                <div class="col-sm-10">
                  <select v-model="course.charge" class="form-control">
                    <option v-for="o in COURSE_CHARGE" v-bind:value="o.key">{{o.value}}</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">状态</label>
                <div class="col-sm-10">
                  <select v-model="course.status" class="form-control">
                    <option v-for="o in COURSE_STATUS" v-bind:value="o.key">{{o.value}}</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">报名数</label>
                <div class="col-sm-10">
                  <input v-model="course.enroll" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">顺序</label>
                <div class="col-sm-10">
                  <input v-model="course.sort" class="form-control" disabled>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button v-on:click="save()" type="button" class="btn btn-primary">保存</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <div id="course-content-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">内容编辑</h4>
          </div>
          <div class="modal-body">
              <div class="form-group">
                <div class="col-lg-12">
                  {{saveContentLabel}}
                </div>
              </div>
              <div class="form-group">
                <div class="col-lg-12">
                  <div id="content"></div>
                </div>
              </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button v-on:click="saveContent()" type="button" class="btn btn-primary">保存</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    <div id="course-sort-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">排序</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label class="control-label col-lg-3">当前排序</label>
                <div class="col-lg-9">
                  <input class="form-control" v-model="sort.oldSort" name="oldSort">
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-lg-3">新排序</label>
                <div class="col-lg-9">
                  <input class="form-control" v-model="sort.newSort" name="newSort">
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button v-on:click="updateSort()" type="button" class="btn btn-primary">保存</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->


  </div>
</template>

<script>
    import Pagination from "../../components/pagination";
    import File from "../../components/uploadFile";
    export default {
        components: {Pagination,File},
        name: "business-course",
        data: function() {
            return {
            course: {},
            courses: [],
            COURSE_LEVEL: COURSE_LEVEL,
            COURSE_CHARGE: COURSE_CHARGE,
            COURSE_STATUS: COURSE_STATUS,
            categorys: [],
            saveContentLabel: '',
            sort: {
                id: "",
                oldSort: 0,
                newSort: 0,
            },
            teachers:[],
            FILE_USE:FILE_USE,
            files:[],
        }
        },
        mounted: function() {
            let _this = this;
            _this.$refs.pagination.size = 5;
            _this.allCategory();
            _this.list(1);
            _this.allTeacher();

        },
        methods: {
            afterUpdate(resp){
                let _this = this;
                let image = resp.content.path;
                _this.course.image = image;
            },

            /**
             *打开内容编辑器
             * */
            //
            // editContent(course){
            //     let _this = this;
            //     let id = course.id;
            //     _this.course = course;
            //     $("#content").summernote({
            //         focus: true,
            //         height: 300
            //     });
            //     // 先清空历史文本
            //     $("#content").summernote('code','');
            //     _this.saveContentLabel = '';
            //     Loading.show();
            //     _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/find-content/'+id).then((response)=>{
            //         Loading.hide();
            //         let resp = response.data;
            //         if (resp.success) {
            //             $("#course-content-modal").modal({backdrop:'static',keyboard:false});
            //             if(resp.content){
            //                 $("#content").summernote('code',resp.content.content);
            //             }
            //             //定时保存
            //             let saveContentInterval = setInterval(function () {
            //                 _this.saveContent();
            //             },10000);
            //             //关闭任务框时 停止自动保存功能
            //             $("#course-content-modal").on('hidden.bs.modal',function (e) {
            //                 clearInterval(saveContentInterval);
            //             })
            //
            //         } else {
            //             Toast.warning(resp.message)
            //         }
            //     })
            // },

            saveContent(){
                let _this = this;
                let content = $("#content").summernote("code");
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/save-content', {
                    id: _this.course.id,
                    content: content,
                }).then((response)=>{
                    Loading.hide();
                    let resp = response.data;
                    if (resp.success) {
                        // Toast.success("保存成功！");
                        let now = Tool.dateFormat("mm:ss");
                        _this.saveContentLabel = "最后保存时间: " + now;
                    } else {
                        Toast.warning(resp.message)
                    }
                })


            },
            initTree(){
                let _this = this;
                let setting = {
                    check: {
                        enable: true
                    },
                    data: {
                        simpleData: {
                            idKey: "id",
                            pIdKey:"parent",
                            rootPid:"00000000",
                            enable: true,
                            tree:{},
                        }
                    }
                };

                let zNodes =_this.categorys;
                _this.tree = $.fn.zTree.init($("#tree"),setting,zNodes);

            },
            /**
             * 列表查询
             */
            allCategory() {
                let _this = this;
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/category/all').then((response)=>{
                    Loading.hide();
                    let resp = response.data;
                    _this.categorys = resp.content;
                    _this.initTree();

                })
            },
            /**
             * 老师查询
             */
            allTeacher() {
                let _this = this;
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/teacher/all').then((response)=>{
                    Loading.hide();
                    let resp = response.data;
                    _this.teachers = resp.content;
                })
            },

            /**
             * 点击【新增】
             */
            add() {
                let _this = this;
                _this.course = {
                    sort:_this.$ref.pagination.total + 1
                };
                _this.tree.checkAllNodes(false);  //新增时所有节点都不选中
                $("#form-modal").modal("show");
            },
            /*
            *   打开模态框
            * */
            openSortModal(course){
                let _this = this;
              _this.sort ={
                  id : course.id,
                  oldSort: course.sort,
                  newSort: course.sort
              }
              $("#course-sort-modal").modal("show");
            },
            /**
             * 排序
             */
            updateSort(){
                let _this = this;
                if(_this.sort.newSort === _this.sort.oldSort){
                    Toast.warning("排序没有变化");
                    return;
                }
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/sort',_this.sort).then((response)=>{
                   let resp = response.data;
                   if(resp.success){
                       Toast.success("更新排序成功");
                       $("#course-sort-modal").modal("hide");
                       _this.list(1);
                   }else{
                       Toast.error("更新排序失败");
                   }
                })
            },
            /**
             * 点击【编辑】
             */
            edit(course) {
                let _this = this;

                _this.course = $.extend({}, course);
                _this.listCategory(course.id);
                $("#form-modal").modal("show");
            },

            /**
             * 列表查询
             */
            list(page) {
                let _this = this;
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/list', {
                    page: page,
                    size: _this.$refs.pagination.size,
                }).then((response)=>{
                    Loading.hide();
                    let resp = response.data;
                    _this.courses = resp.content.list;
                    _this.$refs.pagination.render(page, resp.content.total);
                })
            },

            /**
             * 点击【保存】
             */
            save() {
                let _this = this;

                // 保存校验
                if (1 != 1
                    || !Validator.require(_this.course.name, "名称")
                    || !Validator.length(_this.course.name, "名称", 1, 50)
                    || !Validator.length(_this.course.summary, "概述", 1, 2000)
                    || !Validator.length(_this.course.image, "封面", 1, 100)
                ) {
                    return;
                }
                let categorys = _this.tree.getCheckedNodes();
                if(Tool.isEmpty(_this.course)){
                    Toast.warning("请选择分类");
                    return;
                }
                _this.course.categorys = categorys;

                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/save', _this.course).then((response)=>{
                    Loading.hide();
                    let resp = response.data;
                    if (resp.success) {
                        $("#form-modal").modal("hide");
                        _this.list(1);
                        Toast.success("保存成功！");
                    } else {
                        Toast.warning(resp.message)
                    }
                })
            },

            /**
             * 点击【删除】
             */
            del(id) {
                let _this = this;
                Confirm.show("删除课程后不可恢复，确认删除？", function () {
                    Loading.show();
                    _this.$ajax.delete(process.env.VUE_APP_SERVER + '/business/admin/course/delete/' + id).then((response)=>{
                        Loading.hide();
                        let resp = response.data;
                        if (resp.success) {
                            _this.list(1);
                            Toast.success("删除成功！");
                        }
                    })
                });
            },

            /**
             * 点击【大章】
             */
            toChapter(course) {
                let _this = this;
                SessionStorage.set(SESSION_KEY_COURSE,course);
                _this.$router.push("/business/chapter")
            },
            /**
             * 点击【内容】
             */
            toContent(course) {
                let _this = this;
                SessionStorage.set(SESSION_KEY_COURSE,course);
                _this.$router.push("/business/content")
            },
            /**
             * 查找课程下素有分类
             */
            listCategory(courseId){
                let _this = this;
                Loading.show();
                //将所有勾选到的分类取消
                _this.tree.checkAllNodes(false);
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/list-category/'+courseId, ).then((response)=>{
                    Loading.hide();
                    let resp = response.data;
                    let categorys = resp.content;
                    for (let i = 0; i < categorys.length; i++) {
                        let node = _this.tree.getNodeByParam("id",categorys[i].categoryId);
                        _this.tree.checkNode(node,true);
                    }

                })
            },

        }
    }
</script>