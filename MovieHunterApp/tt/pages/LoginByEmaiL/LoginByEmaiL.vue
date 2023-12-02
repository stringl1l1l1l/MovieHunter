<template>
	<view class="page">
		
		  
		 <view > 
		    <u-tabs :list="list" :is-scroll="false" :current="current" @click="tabChange"  lineWidth="45"
        lineColor="#f56c6c"class="tabs" inactive-color="#ffffff"></u-tabs>
		</view>
		<view v-show="isShow"  >
			<u-form :model="form" :rules="rules" ref="uForm">
			<u-row gutter="16" justify="center">	
				<u-col >
						<u-form-item label="邮箱" prop="email" :required="true">
							<u-input v-model="form.email"  placeholder="请输入邮箱"  />
						</u-form-item>
				</u-col>
				</u-row>
				<u-row gutter="16" justify="center">	
				<u-col>
					<u-form-item label="密码" prop="password">
						<u-input v-model="form.password" placeholder="请输入密码" type="password"></u-input>
					</u-form-item>
				</u-col>
				</u-row>
					 </u-form>
					 <button @click="loginBypw" type="primary" class="button" >登录</button>
					 <view >
						 <u-button @click="login" class="button">没有账号？点我注册</u-button>
						 <u-button @click="findPassword" text="找回密码" class="button"></u-button>
						 </view>
					 </view>
			<view v-show="!isShow">
				<u-row gutter="16" justify="center">
					<u-col span="7">
						<u-form :model="form" :rules="rules" ref="uForm">
							<u-form-item label="邮箱" prop="email">
								<u-input v-model="form.email"  placeholder="请输入邮箱"  />
							</u-form-item>
						 </u-form>
					</u-col>
					
					<u-col span="5">
						 <div v-show="!isFirst">{{ remainingTime }}s后重新获取</div>
						      <u-button
						        :disabled="isCounting"
						        @click="submitByEmail"
						      >
						       发送验证码
						      </u-button>
					</u-col>	
				</u-row>
				<u-row gutter="16" justify="center">
				<view>请输入验证码</view>
				</u-row>
				<u-input v-model="code" type="digit" placeholder="请输入验证码"></u-input>
				<button @click="loginByCode" type="primary" class="button" >登录</button>
			</view>	
	
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
				intervalId: null,
			
			
				code:'',
				 buttonText: '获取验证码',
				list: [{
						name: '密码登录'}, 
						{
						name: '验证码登录'
						}],
				current: 0,		
				isShow: true,
				form:{
					email:"",
					password:""
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
				
				
			};
		},
		methods:{
			submitByEmail(){
				this.$refs.uForm.validate().then(valid => {
								if (valid) {
									console.log('验证通过');
									var email = this.form.email.replace('@','%40')
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
									console.log(this.form.email)
									
									
								} else {
									console.log('验证失败');
								}
							}).catch((e) => {console.log('验证完成');
						console.log((e))});
				
				},
				findPassword(){
					uni.navigateTo({
						url:'/pages/findPassword/findPassword'
					})
				},
			loginBypw(){
				console.log(this.form.email)
				console.log((this.form.password))
				uni.request({
				 	url:this.$BASE_URL.BASE_URL+'loginWithPwd',
				 	method:'POST',
					dataType:'json',
				 	data:{
				 		"email":this.form.email,	
				 		"password":this.form.password
				 	},
				 	success(res) {
				 		console.log(res)
						if(res.data.code==200)
						{
							uni.setStorageSync('token',res.data.data.token)
							console.log('设置token成功');
							uni.showToast({
								title: '登录成功'
							});
							console.log('即将跳转');
							uni.switchTab({
								url:'/pages/recommendations/recommendations'
							})
						}
						else{
							uni.showToast({
								title:res.data.message,
								icon:'none'
							})
						}
						
				 	},
				 	fail(res) {
						uni.showToast({
							icon:'fail',
							title:"网络错误"
						})
				 		console.log(res)
				 	},
							complete() {
								console.log("complete")
							}
				 })
				
			},
			loginByCode(){
				uni.request({
				 	url:this.$BASE_URL.BASE_URL+'loginWithCode',
				 	method:'POST',
					dataType:'json',
				 	data:{
				 		email:this.form.email,	
				 		code:this.code
				 	},
				 	success(res) {
						if(res.data.code==200)
						{
							uni.setStorageSync('token',res.data.data.token)
							console.log('设置token成功');
							uni.showToast({
								title: '登录成功'
							});
							console.log('即将跳转');
							uni.switchTab({
								url:'/pages/recommendations/recommendations'
							})
						}else{
							uni.showToast({
								title:res.data.message,
								icon:'error'
							})
						}
				 		
						
				 	},
				 	fail(res) {
				 		console.log(res)
				 	},
							complete(res) {
								console.log(res)
							}
				 })
				
			},
			login(){
				uni.navigateTo({
					url:'/pages/Login/login'
				})
				
			},
			handle(e){
				this.code= e
				console.log("输入结束，当前值为："+e)
			},
		tabChange(index) {
						this.current = index.index;
						this.isShow = this.current==0;
					},
		onReady() {
				this.$refs.uForm.setRules(this.rules);
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
		
	}
	}
</script>

<style lang="scss">

.tabs{
	display: flex;
	justify-content: center; /* 水平居中 */
	
	  
}
page,body {
	background: url('../../static/bg.jpg') no-repeat center center fixed ; //参数解释请往下看
	background-size: cover;
	box-sizing: border-box;
}
.button{
	opacity: 0.5;
}



</style>
