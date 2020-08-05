<template>
  <div class="main-container">
    <div class="main-content">
      <div class="row">
        <div class="col-sm-10 col-sm-offset-1">
          <div class="login-container">
            <div class="center">
              <h1>
                <i class="ace-icon fa fa-leaf green"></i>
                <span class="black" id="id-text2">登录页面</span>
              </h1>
            </div>

            <div class="space-6"></div>

            <div class="position-relative">
              <div id="login-box" class="login-box visible widget-box no-border">
                <div class="widget-body">
                  <div class="widget-main">
                    <h4 class="header blue lighter bigger">
                      <i class="ace-icon fa fa-coffee green"></i>
                      请输入用户名和密码
                    </h4>

                    <div class="space-6"></div>

                    <form>
                      <fieldset>
                        <label class="block clearfix">
                          <span class="block input-icon input-icon-right">
                              <input v-model="user.loginName" type="text" class="form-control" placeholder="Username"/>
                              <i class="ace-icon fa fa-user"></i>
                          </span>
                        </label>

                        <label class="block clearfix">
                          <span class="block input-icon input-icon-right">
                              <input v-model="user.password" type="password" class="form-control" placeholder="Password"/>
                              <i class="ace-icon fa fa-lock"></i>
                          </span>
                        </label>

                        <div class="space"></div>

                        <div class="clearfix">
                          <label class="inline">
                            <input type="checkbox" class="ace"/>
                            <span class="lbl"> 记住我</span>
                          </label>

                          <button @click="login()" type="button" class="width-35 pull-right btn btn-sm btn-primary">
                            <i class="ace-icon fa fa-key"></i>
                            <span class="bigger-110">登录</span>
                          </button>
                        </div>

                        <div class="space-4"></div>
                      </fieldset>
                    </form>


                    <div class="space-6"></div>

                  </div><!-- /.widget-main -->
                </div><!-- /.widget-body -->
              </div><!-- /.login-box -->
            </div><!-- /.position-relative -->
          </div>
        </div><!-- /.col -->
      </div><!-- /.row -->
    </div><!-- /.main-content -->
  </div><!-- /.main-container -->
</template>

<script>

    export default {
        name: 'login',
        data: function(){
            return {
                user: {},
            }
        },
        methods: {
            login() {

                let _this = this;

                // 保存校验
                if (1 != 1
                    || !Validator.require(_this.user.loginName, "登陆名")
                    || !Validator.length(_this.user.loginName, "登陆名", 1, 50)
                    || !Validator.require(_this.user.password, "密码")
                ) {
                    return;
                }
                _this.user.password = hex_md5(_this.user.password + KEY);
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/login', _this.user).then((response) => {
                    Loading.hide();
                    let resp = response.data;
                    if (resp.success) {
                        console.log(resp.content);
                        $("#form-modal").modal("hide");
                        // SessionStorage.set("USER",resp.content);
                        Tool.setLoginUser(resp.content);
                        _this.$router.push("/welcome");
                        Toast.success("保存成功！");
                    } else {
                        Toast.warning(resp.message)
                    }
                })
            },
        },
        mounted: function () {
            $('body').removeClass('no-skin');
            $('body').attr('class', 'login-layout light-login');
        }
    }

</script>

<style>

</style>
