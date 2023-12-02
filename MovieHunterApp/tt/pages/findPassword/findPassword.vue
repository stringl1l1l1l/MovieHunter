<template>
	<view>
		<view class="register-container">
		  <u-form :rules="rules" :model="form" ref="uForm">
		  <u-form-item label="邮箱" prop="userInfo.email">
		    <u-input v-model="form.userInfo.email" type="text" placeholder="请输入邮箱" />
		  </u-form-item>
		    <u-form-item label="密码" prop="userInfo.password">
		      <u-input v-model="form.userInfo.password" type="password" placeholder="请输入密码" />
		    </u-form-item>
		    <u-form-item label="确认密码" prop="confirmPassword">
		      <u-input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" />
		    </u-form-item>
			  <div v-show="!isFirst">{{ remainingTime }}s后重新获取</div>
			       <u-button
			         :disabled="isCounting"
			         @click="submitByEmail"
			       >
			        发送验证码
			       </u-button>
				   <view>请输入验证码</view>
				   <u-input v-model="form.code" placeholder="请输入验证码" type="digit"></u-input>
		    <u-form-item>
		      <u-button type="primary" @click="resetPassword" text="修改密码"></u-button>
		    </u-form-item>
			  
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
				    { min: 4, max: 16, message: '密码长度为4到16个字符', trigger: 'blur' }
				  ],
				  confirmPassword: [
				    { required: true, message: '请再次输入密码', trigger: 'blur' },
				   {
				   							trigger: 'blur',
				   							validator: (rule, value, callback) => {
				   								if (value !== this.form.userInfo.password) {
				   									callback(new Error('两次输入密码不一致'))
				   								} else {
				   									callback()
				   								}
				   							}
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
				resetPassword() {
						  console.log("注册")
						  // 处理注册逻辑
						
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
									uni.showToast({
										title: '修改成功'
									});
									uni.redirectTo({
										 url:'/pages/LoginByEmaiL/LoginByEmaiL'
									})
								}
								else{
									uni.showToast({
										title:res.data.message,
										icon:'none'
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
				
						
				}
		}
	}
</script>

<style>

</style>
