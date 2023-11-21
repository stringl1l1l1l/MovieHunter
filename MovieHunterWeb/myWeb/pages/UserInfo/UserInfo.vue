<template>
	<view>
		<meta name="referrer" content="no-referrer" />
		<div>
		        <div class="header">
		            <div class="box">
		                <nav class="nav_link">
		                    <img src="../../static/home.png" class="img_btn">
		                    <router-link class="link_btn" active-class="active" to="../recommendations/recommendations">首页</router-link>
		                </nav>
		            </div>
		            <div class="box">
		                <nav class="nav_link">
		                    <img src="../../static/movieTag.png" class="img_btn">
		                    <router-link class="link_btn" active-class="active" to="../favorite/favorite">收藏夹</router-link>
		                </nav>
							 
		                <nav class="nav_link">
		                    <img src="../../static/user.png" class="img_btn">
		                    <router-link class="link_btn" active-class="active" to="../UserInfo/UserInfo">个人主页</router-link>
		                </nav>
							 
							 <nav class="nav_link">
							     <img src="../../static/exit.png" class="img_btn">
							     <router-link class="link_btn" active-class="active" to="../Login/LoginByEmail">退出</router-link>
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
			<u-cell title="邮箱地址" :value="userinfo.email"></u-cell>
			<u-cell title="收藏夹" arrow-direction="left" @click="showFavorite"></u-cell>
		</u-cell-group>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				token:'',
				userinfo:{}
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
			}
			
			
			
		})
	},
	methods:
	{
		showFavorite(){
			uni.navigateTo({
				url:'/pages/favorite/favorite'
			})
		}
	}
	}
</script>

<style>
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
