(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-Login-login"],{"4dfd":function(o,e,t){"use strict";t.r(e);var n=t("546b"),r=t("5b9a");for(var a in r)["default"].indexOf(a)<0&&function(o){t.d(e,o,(function(){return r[o]}))}(a);t("e93c");var s=t("f0c5"),i=Object(s["a"])(r["default"],n["b"],n["c"],!1,null,"16c72d32",null,!1,n["a"],void 0);e["default"]=i.exports},"546b":function(o,e,t){"use strict";t.d(e,"b",(function(){return r})),t.d(e,"c",(function(){return a})),t.d(e,"a",(function(){return n}));var n={uForm:t("49cb").default,uFormItem:t("e9fb").default,uInput:t("d2a3").default,uRow:t("1948").default,uCol:t("5d14").default,uButton:t("8b82").default},r=function(){var o=this,e=o.$createElement,t=o._self._c||e;return t("v-uni-view",{staticClass:"center-container"},[t("v-uni-view",{staticClass:"register-container"},[t("u-form",{ref:"uForm",attrs:{rules:o.rules,model:o.form}},[t("u-form-item",{attrs:{label:"邮箱",prop:"userInfo.email"}},[t("u-input",{attrs:{type:"text",placeholder:"请输入邮箱"},model:{value:o.form.userInfo.email,callback:function(e){o.$set(o.form.userInfo,"email",e)},expression:"form.userInfo.email"}})],1),t("u-form-item",{attrs:{label:"密码",prop:"userInfo.password"}},[t("u-input",{attrs:{type:"password",placeholder:"请输入密码"},model:{value:o.form.userInfo.password,callback:function(e){o.$set(o.form.userInfo,"password",e)},expression:"form.userInfo.password"}})],1),t("u-form-item",{attrs:{label:"确认密码",prop:"confirmPassword"}},[t("u-input",{attrs:{type:"password",placeholder:"请再次输入密码"},model:{value:o.form.confirmPassword,callback:function(e){o.$set(o.form,"confirmPassword",e)},expression:"form.confirmPassword"}})],1),t("u-row",{attrs:{gutter:"16",justify:"center"}},[t("u-col",{attrs:{span:"8"}},[t("u-form",{ref:"uForm",attrs:{model:o.form,rules:o.rules}},[t("u-form-item",{attrs:{label:"验证",prop:"email"}},[t("u-input",{attrs:{placeholder:"请输入验证码",type:"digit"},model:{value:o.form.code,callback:function(e){o.$set(o.form,"code",e)},expression:"form.code"}})],1)],1)],1),t("u-col",{attrs:{span:"4"}},[t("div",{directives:[{name:"show",rawName:"v-show",value:!o.isFirst,expression:"!isFirst"}]},[o._v(o._s(o.remainingTime)+"s后重新获取")]),t("u-button",{staticClass:"code-button",attrs:{disabled:o.isCounting},on:{click:function(e){arguments[0]=e=o.$handleEvent(e),o.submitByEmail.apply(void 0,arguments)}}},[o._v("发送验证码")])],1)],1),t("v-uni-button",{staticClass:"login-button",attrs:{type:"primary"},on:{click:function(e){arguments[0]=e=o.$handleEvent(e),o.register.apply(void 0,arguments)}}},[o._v("注册并登录")])],1)],1)],1)},a=[]},"5b9a":function(o,e,t){"use strict";t.r(e);var n=t("f640"),r=t.n(n);for(var a in n)["default"].indexOf(a)<0&&function(o){t.d(e,o,(function(){return n[o]}))}(a);e["default"]=r.a},"622e":function(o,e,t){var n=t("24fb"),r=t("1de5"),a=t("b1f5");e=n(!1);var s=r(a);e.push([o.i,".center-container[data-v-16c72d32]{display:flex;justify-content:center;align-items:center;height:95.9vh;background-image:url("+s+");background-repeat:no-repeat;background-size:100% 100%;background-position:bottom}.register-container[data-v-16c72d32]{width:30%; /* 设置矩形框的宽度，可以根据需要进行调整 */padding:10px;border-radius:8px;background-color:#f8f8ff}\r\n\r\n/* 登录按钮 */.login-button[data-v-16c72d32]{background-color:#a0deaa;transition:background-color .3s /* 添加过渡效果 */}.login-button[data-v-16c72d32]:hover{background-color:#82c98f /* 鼠标停留时的背景颜色 */}.login-button[data-v-16c72d32]:active{background-color:#5e9e68 /* 鼠标点击时的背景颜色 */}.code-button[data-v-16c72d32]{background-color:#fff;transition:background-color .3s /* 添加过渡效果 */}.code-button[data-v-16c72d32]:hover{background-color:#ebebeb /* 鼠标停留时的背景颜色 */}.code-button[data-v-16c72d32]:active{background-color:#e2e2e2 /* 鼠标点击时的背景颜色 */}",""]),o.exports=e},d64b:function(o,e,t){var n=t("622e");n.__esModule&&(n=n.default),"string"===typeof n&&(n=[[o.i,n,""]]),n.locals&&(o.exports=n.locals);var r=t("4f06").default;r("7f661a65",n,!0,{sourceMap:!1,shadowMode:!1})},e93c:function(o,e,t){"use strict";var n=t("d64b"),r=t.n(n);r.a},f640:function(o,e,t){"use strict";t("7a82"),Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0,t("d9e2"),t("d401"),t("ac1f"),t("5319"),t("d3b7"),t("25f0"),t("99af");var n={data:function(){var o=this;return{isCounting:!1,remainingTime:60,isFirst:!0,form:{userInfo:{email:"",password:""},confirmPassword:"",code:""},rules:{password:[{required:!0,message:"请输入密码",trigger:"blur"},{min:6,max:16,message:"密码长度为6到16个字符",trigger:"blur"}],confirmPassword:[{required:!0,message:"请再次输入密码",trigger:"blur"},{trigger:"blur",validator:function(e,t,n){t!==o.form.userInfo.password?n(new Error("两次输入密码不一致")):n()}}]}}},methods:{submitByEmail:function(){var o=this;this.$refs.uForm.validate().then((function(e){if(o.isFirst=!1,o.intervalId&&clearInterval(o.intervalId),o.isCounting=!0,o.remainingTime=60,o.intervalId=setInterval((function(){o.remainingTime>0?o.remainingTime--:(o.isCounting=!1,clearInterval(o.intervalId))}),1e3),console.log("提交"),console.log(o.form.userInfo.email),e){console.log("验证通过");var t=o.form.userInfo.email.replace("@","%40");uni.request({url:o.$BASE_URL.BASE_URL+"sendMsg/"+t,method:"GET",success:function(o){uni.showToast({title:"验证码已发送"}),console.log(o)},complete:function(o){console.log(o)}})}else console.log("验证失败")})).catch((function(o){console.log("验证完成"),console.log(o)}))},register:function(){console.log("注册"),uni.request({url:this.$BASE_URL.BASE_URL+"register",method:"POST",dataType:"json",data:{email:this.form.userInfo.email,password:this.form.userInfo.password,code:this.form.code},success:function(o){console.log(o),200==o.data.code?(uni.setStorage({key:"token",data:o.data.data.token,success:function(o){console.log("设置token成功"),uni.showToast({title:"登录成功"}),console.log("即将跳转")},fail:function(){uni.showToast({title:"设置token失败",icon:"error"})}}),uni.redirectTo({url:"/pages/recommendations/recommendations"})):uni.showToast({title:o.data.message,icon:"error"})},complete:function(o){console.log(o)},fail:function(o){console.log(o)}})},handle:function(o){this.code=o.toString(),console.log("输入结束，当前值为："+o)},handleCountDownChange:function(o){var e=o.day,t=o.hour,n=o.minute,r=o.second;this.isCounting&&(this.buttonText="".concat(n,":").concat(r)),0===e&&0===t&&0===n&&0===r&&(this.isCounting=!1,this.buttonText="获取验证码")}},onReady:function(){this.$refs.uForm.setRules(this.rules)}};e.default=n}}]);