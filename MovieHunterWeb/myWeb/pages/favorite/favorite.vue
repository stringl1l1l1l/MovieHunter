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
		
		
		<u-list>
					 <u-list-item v-for="favorite in favorites" :key="favorite.favoriteId">
						 <u-row justify="between">
						 								 <u-col span="5" @click="showFavoriteMovies(favorite.favoriteId)">
						 								 	{{favorite.name}}
						 								 </u-col>
						 								 <u-col span="1">
															 <u-button text="移除" type="warning" class="button" @click="deleteFavorite(favorite.favoriteId)"></u-button>
														 </u-col>
						 								 
						 </u-row>
						
					 </u-list-item>
		</u-list>.
		
		<u-popup :show="isMoviesShow" :closeable="true" @close="close" closeIconPos="top-left" :safeAreaInsetTop="true">
			<u-list class="movie-list">
						 <u-list-item v-for="movie in movies" :key="movie.movieId">
							 <u-row>
								 <u-col span="5">
								 	{{movie.name}}
								 </u-col>
								 <u-col span="1"></u-col>
								 <u-button text="移除" type="warning" class="button" @click="deleteMovie(movie.movieId)"></u-button>
							 </u-row>
							
						 </u-list-item>
			</u-list>.
		</u-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				favorites:[],
				userId:'',
				movies:[],
				isMoviesShow:false,
				token:'',
				selectFavoriteId:0,
				userinfo:{}
			}
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
						that.userId = res.data.data.userId
						that.showFavorite()
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
			
			
			
		})},
		methods: {
			deleteFavorite(e)
			{
				console.log(e)
				var that=this
				uni.showModal({
					title:"确认删除？",
					success(res) {
						if(res.confirm)
						{
							uni.request({
								url:that.$BASE_URL.BASE_URL+'favorite/deleteFavoriteById/'+e,
								method:'DELETE',
								header:{token:that.token},
								success(res) {
									if(res.data.code==200)
									{
										uni.showToast({
											title:"删除成功"
										})
									}
									that.showFavorite()
								},
								complete(res) {
										console.log(res)
								}
							})
						}
					}
				})
			},
			showFavorite()
			{
				var that = this
				uni.request({
								url:this.$BASE_URL.BASE_URL + 'favorite/insertFavorite/'+that.userId,
								header:{token:that.token},
								method:'GET',
								success(res) {
									if(res.data.code==200){
										that.favorites = res.data.data
									}else{
										console.log(res)
									}
								},
								fail(res) {
												console.log(res)
												uni.showToast({
													title:"网络出错了",
													icon:'error'
												})
											},
											complete(res) {
												console.log(res)
											}
							})
			},
			showFavoriteMovies(e)
			{
				console.log(e)
				var that=this
				this.selectFavoriteId = e
				uni.request({
					url:this.$BASE_URL.BASE_URL + 'favorite/findAllMoviesInFavorite/'+e,
					method:'GET',
					header:{token:that.token},
					success(res) {
						if(res.data.code==200)
						{
							that.movies = res.data.data
							if(res.data.data.length==0)
							{
								uni.showToast({
									title:"暂无电影",
									icon:"error"
								})
							}
							else{
								that.isMoviesShow=true
							}
							
						}else{
							console.log(res)
						}
					},
					fail(res) {
							console.log(res)
							uni.showToast({
								title:"网络出错了",
									icon:'error'
							})
							},
							complete(res) {
								console.log(res)
							}
				})
			},
			close() {
				this.isMoviesShow = false
			},
			deleteMovie(e)
			{
				console.log(e)
				var that=this
				uni.request({
					url:this.$BASE_URL.BASE_URL+'favorite/deleteMovieInFavorite',
					method:'DELETE',
					header:{token:that.token},
					data:{
						favoriteId:that.selectFavoriteId,
						movieId:e
						
					},
					success(res){
						if(res.data.code==200)
						{
							uni.showToast({
								title:"移除成功"
							})
						}else{
							console.log(res)
						}
						
					},
					fail(res) {
							console.log(res)
							uni.showToast({
								title:"网络出错了",
									icon:'error'
							})
							},
							complete(res) {
								console.log(res)
							}
					
				}),
				this.showFavoriteMovies(this.selectFavoriteId)
			},
			
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
.movie-list {
	  display: flex;
	  flex-direction: column;
	  align-items: center;
	  padding: 20px;
	  z-index: 0;
	}
.movie-item {
  width: 100%;
  max-width: 600px;
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  margin-bottom: 20px;
  padding: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

</style>
