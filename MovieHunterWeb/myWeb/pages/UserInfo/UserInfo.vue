<template>
	<view class="main">
		<meta name="referrer" content="no-referrer" />
		<div>
		        <div class="header">
		            <div class="box">
		                <nav class="nav_link">
		                    <img src="../../static/home.png" class="img_btn">
		        						 <text class="link_btn"  @click="handleClick('recommendations/recommendations')">首页</text>
		                    </nav>
		            </div>
		            <div class="box">
		                <nav class="nav_link">
		                    <img src="../../static/movieTag.png" class="img_btn">
		        						 <text class="link_btn"  @click="handleClick('favorite/favorite')">收藏夹</text>
		                </nav>
		        			 
		                <nav class="nav_link">
		                    <img src="../../static/user.png" class="img_btn">
		                    <text class="link_btn" @click="handleClick('UserInfo/UserInfo')">个人主页</text>
		                </nav>
		        			 
		        			 <nav class="nav_link">
		        			     <img src="../../static/exit.png" class="img_btn">
		        			     <text class="link_btn" @click="handleClick('Login/LoginByEmail')">退出</text>
		        			 </nav>
		            </div>
		        </div>
		    </div>
		

		<u-cell-group>
			<u-cell title="个人头像" >
				<template #right-icon>
					<u-avatar
						shape="square"
						size="35"
						:src="userinfo.avatar"
						customStyle="margin: -3px 5px -3px 0"
					></u-avatar>
				</template>
			</u-cell>
			<u-cell title="用户名" :value="userinfo.username"></u-cell>
			<u-cell title="邮箱地址" :value="userinfo.email"></u-cell>
			<u-cell title="收藏夹" arrow-direction="left" @click="showFavorite"></u-cell>
			<u-cell title="点赞记录" arrow-direction="left" @click="showLike"></u-cell>
			<u-cell title="评论记录" arrow-direction="left" @click="showComment"></u-cell>
		</u-cell-group>
		<button class="logout-button" type="warning" @click="Popup">修改用户名</button>
		<button class="logout-button" type="warning" @click="changePwd">修改密码</button>
		<button class="logout-button" type="warning" @click="deleteUser">注销账号</button>
		
		<view v-if="showPopup" class="overlay">
				<div class="sub-window">
					<div @click="closePopup()" class="close-button">&times;</div>
					  <input type="text" v-model="inputText" placeholder="请输入新名称" style="margin-top: 10pt;">
					  <button class="submit-button" @click="submitText" style="margin-top: 10pt;">提交</button>
					
				</div>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				token:'',
				userinfo:{},
				isCreateShow:false,
				showPopup: false,
				inputText: '',
				form:{
							  userInfo:{
											oldPwd:'',
							  			  email:'',
							  			  password:''
							  },
							  confirmPassword: '', 
							  code: ''
				},
						 
				rules: {
					oldPwd: [
					  { required: true, message: '请输入密码', trigger: 'blur' }
					],
				  password: [
				    { required: true, message: '请输入密码', trigger: 'blur' },
				    { min: 6, max: 16, message: '密码长度为6到16个字符', trigger: 'blur' }
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
				  ]
				}
			};
		},
		onLoad(){
			var that = this
			uni.getStorage({
				key:'token',
				success(res) {
					that.token=res.data;
				},
					fail(){
						uni.showToast({
							icon:Error,
							title:"登录校验失败，请重新登录"
						})
						uni.redirectTo({
							url:'/pages/Login/LoginByEmail'
						})
					}
			}),
			console.log(this.token)
			uni.request({
				url:this.$BASE_URL.BASE_URL+'user/parseToken/'+that.token,
				method:'GET',
				success(res) {
					if(res.data.code==200)
					{
						console.log(res)
						that.userinfo = res.data.data
						console.log(that.userinfo)
					}else{
						console.log(res)
					}
			},
			fail(res) {
				console.log(res)
				uni.showToast({
					title:"网络出错了",
					icon:'fail'
				})
			}})
	},
	methods:
	{
		handleClick(e){
			uni.redirectTo({
				url:'/pages/'+e
			})
		},
		submitText(){
			// 在这里处理提交逻辑
			var that = this
			// console.log(this.selectFavoriteId)
			// console.log('提交的文本:', this.inputText);
			if (that.inputText=="") {
				that.inputText = "默认"
			}
				uni.request({
					url:this.$BASE_URL.BASE_URL + 'user/updateUserById',
					method:'PUT',
					header:{token:that.token},
					data:{
						userId: that.userinfo.userId,
						email: that.userinfo.email,
						username: that.inputText,
						password: that.userinfo.password
					},
					success(res) {
						if(res.data.code==200){
							that.closePopup();
							uni.showToast({
								title:'修改成功'
							})
							uni.request({
								url:that.$BASE_URL.BASE_URL+'user/parseToken/'+that.token,
								method:'GET',
								success(res) {
									if(res.data.code==200)
									{
										console.log(res)
										that.userinfo = res.data.data
										console.log(that.userinfo)
									}else{
										uni.showToast({
											title:res.data.message,
											icon:'error'
										})
									}
							},
							fail(res) {
								console.log(res)
								uni.showToast({
									title:"网络出错了",
									icon:'fail'
								})
							}})
						}else{
							that.closePopup();
							uni.showToast({
								title:res.data.message,
								icon:'error'
							})
						}
					},
					complete(res) {
						console.log(res)
					}
				})
			this.inputText = '';
		},
		closePopup(){
			this.showPopup = false
		},
		Popup(){
			this.showPopup=true
		},
		change(){
			var that=this
			if(that.form.userInfo.oldPwd != that.userinfo.password){
				this.isCreateShow=false
				uni.showToast({
				  title: '密码错误',
				  icon:"error"
				});
			}else{
				
			}
		},
		changePwd(){
			// this.isCreateShow=true
			uni.redirectTo({
				url:'/pages/Login/findPassword'
			})
		},
		closeSubWindow_create(){
			this.isCreateShow=false
		},
		deleteUser()
		{
			
			var that = this
			console.log(that.userinfo)
			uni.showModal({
			  title: '确认注销用户？',
			  content:'该操作不可撤销！',
			  success(res) {
			    if (res.confirm) {
					
			      uni.request({
			        url: that.$BASE_URL.BASE_URL + 'user/deleteUserById/' + that.userinfo.userId,
			        method: 'DELETE',
			        header: { token: that.token ,
					},
			        success(res) {
			          if (res.data.code == 200) {
			            
						uni.removeStorageSync('token');
						uni.redirectTo({
							url:'/pages/Login/LoginByEmail'
						})
						uni.showToast({
						  title: '注销成功'
						});
			          }else{
						  uni.showToast({
						      title: res.data.message,
						  	icon: 'error'
						  });
					  }
			          
			        },
			        complete(res) {
			          console.log(res);
			        }
			      });
			    }
			  }
			});
		},
		showFavorite(){
			uni.navigateTo({
				url:'/pages/favorite/favorite'
			})
		},
		showLike(){
			uni.navigateTo({
				url:'/pages/UserInfo/like'
			})
		},
		showComment(){
			uni.navigateTo({
				url:'/pages/UserInfo/comment'
			})
		}
	}
	}
</script>

<style>
.close-button {
  position: absolute;
  top:1px;
  right: 8px;
  font-size: 36px;
  color: #999;
  cursor: pointer;
  /* background-color: #333; */
  border-width: 0%;
}
.sub-window {
	position: relative;
	width: 300px;
	 height: 100px;
	 background-color: #fff;
	 border: 1px solid #ccc;
	 padding: 16px;
}
.close-button {
  position: absolute;
  top:1px;
  right: 8px;
  font-size: 36px;
  color: #999;
  cursor: pointer;
  border-width: 0%;
}
.overlay {
  position: fixed;
  z-index: 9999;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5);
}
.sub-window-create {
	position: relative;
	width: 400px;
	 height: 250px;
	 background-color: #fff;
	 border: 1px solid #ccc;
	 padding: 16px;
}
.main {
	background-color: #f5f5f5;
}
.button-container {
  display: flex;
  justify-content: space-between;
}
.header{
	background-image: url("../../static/title.png");
	background-repeat: no-repeat;
	background-size: 38% 100%;
	background-position: bottom;
	display: flex;
	align-items: center;
	justify-content: space-between;
	height: 70px;
	background-color:#edf4ed;
}
.box{
	display: flex;
	align-items: center;
}
.nav_link{
        display: flex;
        align-items: center;
        padding: 0 30px;
}
.link_btn{
	text-decoration:none;
	color: #7F7F7F;
	font-size: 25px;
	font-weight: normal;
}
.img_btn{
	width: 30px;
	height: 30px;
}
.title_btn{
	width: 40%;
	height: 40%;
}
</style>
