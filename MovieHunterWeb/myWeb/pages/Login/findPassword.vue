<template>
	<view class="center-container">
		<view class="register-container">
		  <u-form :rules="rules" :model="form" ref="uForm">
			<u-form-item label="邮箱" prop="userInfo.email">
			  <u-input v-model="form.userInfo.email" type="text" placeholder="请输入邮箱" />
			</u-form-item>
			<u-form-item label="密码" prop="userInfo.password">
			  <u-input v-model="form.userInfo.password" type="password" placeholder="请输入新密码" />
			</u-form-item>
			<u-form-item label="确认密码" prop="confirmPassword">
			  <u-input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" />
			</u-form-item>
			
			<u-row gutter="16" justify="center">
				<u-col span=8>
					<u-form :model="form" :rules="rules" ref="uForm">
						<u-form-item label="验证" prop="email">
							<u-input v-model="form.code"  placeholder="请输入验证码" type="digit" />
						</u-form-item>
					 </u-form>
				</u-col>
				
				<u-col span=4>
					 <div v-show="!isFirst">{{ remainingTime }}s后重新获取</div>
						  <u-button
							:disabled="isCounting"
							@click="submitByEmail"
							class="code-button"
						  >
						   发送验证码
						  </u-button>
				</u-col>	
			</u-row>
			
			<button type="primary" @click="register" class="login-button">更新并登录</button>
			
		  </u-form>
		</view>
  </view>
</template>

<script>
  export default {
    data() {
      return {
		  isCounting: false,
		  remainingTime: 60,
		  isFirst:true,
		  
		  form:{
			  userInfo:{
			  			  email:'',
			  			  password:''
			  },
			  confirmPassword: '', 
			  code: ''
		  },
		 
		  rules: {
		    password: [
		      { required: true, message: '请输入密码', trigger: 'blur' },
		      { min: 6, max: 16, message: '密码长度为6到16个字符', trigger: 'blur' }
		    ],
		    confirmPassword: [
		      { required: true, message: '请再次输入密码', trigger: 'blur' },
		     {
		     							trigger: 'blur',
		     							// validator: (rule, value, callback) => {
		     							// 	if (value !== this.form.userInfo.password) {
		     							// 		callback(new Error('两次输入密码不一致'))
		     							// 	} else {
		     							// 		callback()
		     							// 	}
		     							// }
		     						}
		    ],
			email:[
				{required:true,message:'请输入邮箱',trigger:'blur' ,type:"email"}
			],
		  }
        
      }
    },
    methods: {
		submitByEmail(){
			this.$refs.uForm.validate().then(valid => {
				this.isFirst = false;
				 if (this.intervalId) {
				        clearInterval(this.intervalId); // 清除之前的计时器
				      }
					  
				this.isCounting = true;
				      this.remainingTime = 60; // 重置倒计时秒数
				      this.intervalId = setInterval(() => {
				        if (this.remainingTime > 0) {
				          this.remainingTime--;
				        } else {
				          this.isCounting = false;
				          clearInterval(this.intervalId);
				        }
				      }, 1000); // 每秒更新一次倒计时秒数
				console.log('提交')
				console.log(this.form.userInfo.email)
							if (valid) {
								console.log('验证通过');
								var email = this.form.userInfo.email.replace('@','%40')
								uni.request({
									url: this.$BASE_URL.BASE_URL+'sendMsg/'+email,
									method:'GET',	
									success(res) {
										uni.showToast({
											title:"验证码已发送"
										})
										
										console.log(res)
									},
									complete(e){
										console.log(e)
									}
								})
							} else {
								console.log('验证失败');
							}
						}).catch((e) => {console.log('验证完成');
						console.log((e))});
			
			
			},
      register() {
		  uni.request({
		  	url:this.$BASE_URL.BASE_URL+'resetPwdWithCode',
		  							method:'POST',
		  							dataType:'json',
		  							data:{
		  								email:this.form.userInfo.email,
		  								password:this.form.userInfo.password,
		  								code:this.form.code
		  							},
		  							success(res){
		  								console.log(res)
		  								if(res.data.code==200)
		  								{
		  									uni.setStorage({
		  										key:'token',
		  										data:res.data.data.token,
		  										success(data) {
		  											console.log('设置token成功');
		  											uni.showToast({
		  												title: '登录成功'
		  											});
		  											console.log('即将跳转');
		  										},
		  										fail() {
		  											uni.showToast({
		  												title: '设置token失败',
		  												icon: 'error'
		  											});
		  										}
		  									})
		  									uni.switchTab({
		  										url:'/pages/recommendations/recommendations'
		  									})
		  								}
		  								else{
		  									uni.showToast({
		  										title:"验证码错误",
		  										icon: 'error'
		  										
		  									})
		  								}
		  							},
		  							complete(res) {
		  								console.log(res)
		  							},
		  							fail(err) {
		  								console.log(err)
		  							}
		  							
		  							
		  })
      
		
      },
	 handle(e){
	 	this.code= e.toString()
	 	console.log("输入结束，当前值为："+e)
	 },
	  handleCountDownChange({ day, hour, minute, second }) {
	         if (this.isCounting) {
	           this.buttonText = `${minute}:${second}`; // 实时更新按钮文本为倒计时剩余时间
	         }
	         if (day === 0 && hour === 0 && minute === 0 && second === 0) {
	           this.isCounting = false; // 倒计时结束
	           this.buttonText = '获取验证码'; // 恢复按钮文本为默认值
	         }
	       }
	  
    },
	onReady() {
			this.$refs.uForm.setRules(this.rules);
		},
		
  }
</script>

<style>
	
.center-container {
	display: flex;
	  justify-content: center;
	  align-items: center;
	  height: 95.9vh;
	  
	  background-image: url("../../static/login.jpg");
	  background-repeat: no-repeat;
	  background-size: 100% 100%;
	  background-position: bottom;
}

.register-container {
width: 30%; /* 设置矩形框的宽度，可以根据需要进行调整 */
	padding: 10px;
	border-radius: 8px;
	background-color: ghostwhite;
}

/* 登录按钮 */
.login-button {
	background-color: #a0deaa;
	transition: background-color 0.3s; /* 添加过渡效果 */
}
.login-button:hover {
	background-color: #82c98f; /* 鼠标停留时的背景颜色 */
}
.login-button:active {
	background-color: #5e9e68; /* 鼠标点击时的背景颜色 */
}

.code-button {
	background-color: #ffffff;
	transition: background-color 0.3s; /* 添加过渡效果 */
}
.code-button:hover {
	background-color: #ebebeb; /* 鼠标停留时的背景颜色 */
}
.code-button:active {
	background-color: #e2e2e2; /* 鼠标点击时的背景颜色 */
}

</style>
