<template>
	<view>
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
		
		
		<view class="main">
		<view class="favorites-list">
			<button @click="createBtn()" style="background-color: aliceblue; margin: 10pt;">新建收藏夹</button>
		    <view v-for="favorite in favorites" :key="favorite.favoriteId" class="favorite-item">
		      <view class="container" >
		        <view class="left-section">
					<text class="text" >{{ favorite.name }}</text></view>
				  <view class="right-section">
					  <u-button class="edit-button" @click="showFavoriteMovies(favorite.favoriteId)" text="查看"></u-button>
						<u-button class="edit-button" @click="editItem(favorite.favoriteId)" text="修改"></u-button>
						<u-button class="remove-button" text="移除" type="warning"  @click="deleteFavorite(favorite.favoriteId)"></u-button> 
				  </view>
		        
		      </view>
		    </view>
		  </view>
		</view> 
		
		<view v-if="isCreateShow" class="overlay">
				<div class="sub-window-create">
					<div @click="closeSubWindow_create()" class="close-button">&times;</div>
					<u-input  v-model="favoriteName" placeholder="请输入收藏夹名称"></u-input>
					<u-button @click="createFavorite()" style="margin-top: 10pt;">点击创建</u-button>
					<view v-if="isErrorShow" style="color: red;">{{errorMessage}}</view>
				</div>
		</view>
		
		<view v-if="showPopup" class="overlay">
				<div class="sub-window-create">
					<div @click="closePopup()" class="close-button">&times;</div>
						
					  <input type="text" v-model="inputText" placeholder="请输入新名称" style="margin-top: 10pt;">
					  <button class="submit-button" @click="submitText" style="margin-top: 10pt;">提交</button>
					
				</div>
		</view>
			
		<view v-if="isMoviesShow" class="overlay">
				<div class="sub-window-big">
					<div @click="closeMoviesShow()" class="close-button">&times;</div>
					<div class="scrollable-list" >
					<view class="movie-list" style="margin-top: 10pt;">
					  <view v-for="movie in movies" :key="movie.movieId" class="movie-item-small">
						  <u-row>
							  <u-col span="6">
								  <text class="movie-name" >{{ movie.name }}</text>
							  </u-col>
							  <u-col span="3">
								  <u-button text="移除" type="warning" class="button" @click="deleteMovie(movie.movieId)"></u-button>
							  </u-col>
							  <u-col span="3">
									<u-button text="移动" type="primary" class="button" @click="changeMovie(movie.movieId)"></u-button>
							  </u-col>
						  </u-row>
						
					  </view>
					</view>
					</div>
				</div>
		</view>
		
		
		  
		<view v-if="showChange" class="overlay">
				<div class="sub-window-big">
					<div @click="closeShowChange()" class="close-button">&times;</div>
					<div class="scrollable-list" >
					<view class="movie-list" style="margin-top: 10pt;">
						
					  <view v-for="favorite in favorites" :key="favorite.favoriteId" class="movie-item-small">
						  <u-row>
							  <u-col span="9">
								  <text class="movie-name" >{{ favorite.name }}</text>
							  </u-col>
							  <u-col span="3">
									<u-button text="移动" type="primary" class="button" @click="confirmChange(favorite.favoriteId)"></u-button>
							  </u-col>
						  </u-row>
					  </view>
					  
					</view>
					</div>
				</div>
		</view>
			
	
		
		</view>
		
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
				userinfo:{},
				showChange:false,
				selectMovieId:0,
				showPopup: false,
				inputText: '',
				isCreateShow: false,
				isErrorShow: false,
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
			}})
			that.updateFavorite()
		},   
		methods: {
			handleClick(e){
				uni.redirectTo({
					url:'/pages/'+e
				})
			},
			updateFavorite() {
					  var that = this
					  uni.request({
						url:this.$BASE_URL.BASE_URL+'favorite/insertFavorite/'+that.userId,
							method:'GET',
							header:{
								token:that.token
								},
								success(res) {
									console.log(res)
									that.favorites = res.data.data
								},
								fail(res) {
									console.log(res)
									uni.showToast({
										title:"网络出错了",
										icon:'fail'
									})
								}
					  })
					// this.showSubWindow()
			},
			showError(e) {
					  this.isErrorShow = true
					  this.errorMessage = e
			},
			createFavorite()
			{
				var that = this
				if (that.favoriteName=="") {
					that.favoriteName = "默认"
				}
				uni.request({
					url:this.$BASE_URL.BASE_URL + 'favorite/insertFavorite',
					method:'PUT',
					header:{token:that.token},
					data:{
						userId:that.userId,
						name:that.favoriteName
					},
					success(res) {
							if (res.data.code != 200) {
								that.showError(res.data.message)
							}else{
								that.favoriteName = ''
								that.isCreateShow=false
								that.updateFavorite()
								uni.showToast({
									title:'创建成功'
								})
							}
					},
					fail(res) {
							that.showError("网络出错了")
					},
				})
			},
			closeSubWindow_create(){
					  this.isCreateShow = false
					  this.isErrorShow=false
			},
			createBtn() {
				var that = this
				that.isCreateShow=true
			},
			confirmChange(e){
					  console.log(e)
					  var that=this
					  uni.request({
					  	url:this.$BASE_URL.BASE_URL + 'favorite/moveMovieToFavorite/' + e,
						method:'POST',
						header:{token:that.token},
						data:{
							favoriteId:that.selectFavoriteId,
							movieId:that.selectMovieId
						},
						success(res) {
							if(res.data.code==200){
								that.closeShowChange()
								that.closeMoviesShow()
								uni.showToast({
									title:'移动成功'
								})
							}
							else{
								that.closeShowChange()
								that.closeMoviesShow()
								uni.showToast({
								  title: res.data.message,
								  icon: 'error'
								});
							}
							
						},
						fail(res) {
							that.closeShowChange()
						  that.closeMoviesShow()
						  uni.showToast({
						    title: '网络出错了',
						    icon: 'error'
						  });
						},
					  })
			},
			 
			  changeMovie(e)
			  {
				  this.isMoviesShow = false
			      this.showChange = true
				  this.selectMovieId = e
			  },
			  editItem(e)
			  {
				  this.showPopup = true;
				  this.selectFavoriteId = e
				  console.log(this.userId)
			  },
			  submitText() {
			  // 在这里处理提交逻辑
			  var that = this
			  console.log(this.selectFavoriteId)
			  console.log('提交的文本:', this.inputText);
			  if (that.inputText=="") {
				  that.inputText = "默认"
			  }
				uni.request({
					url:this.$BASE_URL.BASE_URL + 'favorite/updateFavouriteById',
					method:'POST',
					header:{token:that.token},
					data:{
						favoriteId:that.selectFavoriteId,
						userId:that.userId,
						name:that.inputText
					},
					success(res) {
						if(res.data.code==200){
							that.showFavorite();
							
			
							uni.showToast({
								title:'修改成功'
							})
							
						}else{
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
			  // 提交完成后关闭弹出框
			  this.showPopup = false;
			  this.inputText = '';
			},
			deleteFavorite(e) {
			  console.log(e);
			  var that = this;
			  uni.showModal({
			    title: '确认删除？',
			    success(res) {
			      if (res.confirm) {
			        uni.request({
			          url: that.$BASE_URL.BASE_URL + 'favorite/deleteFavoriteById/' + e,
			          method: 'DELETE',
			          header: { token: that.token },
			          success(res) {
			            if (res.data.code == 200) {
							that.showFavorite();
			              uni.showToast({
			                title: '删除成功'
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
			showFavorite() {
				
			  var that = this;
			  uni.request({
			    url: this.$BASE_URL.BASE_URL + 'favorite/findFavoritesByCurUser/',
			    header: { token: that.token },
			    method: 'GET',
			    success(res) {
			      if (res.data.code == 200) {
			        that.favorites = res.data.data;
			      } else {
			        console.log(res);
			      }
			    },
			    fail(res) {
			      console.log(res);
			      uni.showToast({
			        title: '网络出错了',
			        icon: 'error'
			      });
			    },
			    complete(res) {
			      console.log(res);
			    }
			  });
			},
			showFavoriteMovies(e) {
				
			  console.log(e);
			  var that = this;
			  this.selectFavoriteId = e;
			  uni.request({
			    url: this.$BASE_URL.BASE_URL + 'favorite/findAllMoviesInFavorite/' + e,
			    method: 'GET',
			    header: { token: that.token },
			    success(res) {
			      if (res.data.code == 200) {
			        that.movies = res.data.data;
			        if (res.data.data.length == 0) {
			          uni.showToast({
			            title: '暂无电影',
			            icon: 'error'
			          });
			        } else {
			          that.isMoviesShow = true;
			        }
			      } else {
			        console.log(res);
			      }
			    },
			    fail(res) {
			      console.log(res);
			      uni.showToast({
			        title: '网络出错了',
			        icon: 'error'
			      });
			    },
			    complete(res) {
			      console.log(res);
			    }
			  });
			},
			closeMoviesShow() {
			  this.isMoviesShow = false;
			},
			closeShowChange(){
				console.log('关闭')
				this.showChange = false
			},
			closePopup(){
				this.showPopup = false
			},
			deleteMovie(e) {
			  console.log(e);
			  var that = this;
			  uni.request({
			    url: this.$BASE_URL.BASE_URL + 'favorite/deleteMovieInFavorite',
			    method: 'DELETE',
			    header: { token: that.token },
			    data: {
			      favoriteId: that.selectFavoriteId,
			      movieId: e
			    },
			    success(res) {
			      if (res.data.code == 200) {
					that.closeMoviesShow()
			        uni.showToast({
			          title: '移除成功'
			        });
			      } else {
			        that.closeMoviesShow()
					uni.showToast({
					  title: res.data.message,
					  icon: 'error'
					});
			      }
			    },
			    fail(res) {
			      that.closeMoviesShow()
			      uni.showToast({
			        title: '网络出错了',
			        icon: 'error'
			      });
			    },
			    complete(res) {
					that.closeMoviesShow()
			      console.log(res);
			    }
			  });
			  this.showFavoriteMovies(this.selectFavoriteId);
			}
		}
		
	}
</script>

<style>
.scrollable-list {
  overflow-y: auto;
  height: 100%;
}
.popup-content {
	margin-top: 20pt;
}
.main {
	display: flex;
	justify-content: center;
	background-color: #f5f5f5;
}
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
.close-button:hover {
  color: #333;
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

.sub-window {
  position: relative;
 width: 300px;
  height: 315px;
  background-color: #fff;
  border: 1px solid #ccc;
  padding: 16px;
}
.sub-window-create {
	position: relative;
	width: 300px;
	 height: 100px;
	 background-color: #fff;
	 border: 1px solid #ccc;
	 padding: 16px;
}
.sub-window-big {
	position: relative;
	width: 600px;
	 height: 700px;
	 background-color: #fff;
	 border: 1px solid #ccc;
	 padding: 16px;
}

.container {
  display: flex;
  align-items: center;
 
  padding: 10px;
 
}
.left-section {
  flex: 1;
}

.favorites-list {
	background-color: #f5f5f5;
	width:50%
}
.remove-button {
  color: #fff;
  border: none;
  
  border-radius: 4px;
  cursor: pointer;
  min-width: 80px; /* 调整按钮的最小宽度 */
   margin-left: 10px; /* 将按钮推至右侧 */
  
}
.edit-button {
  background-color: #007bff;
  color: #fff;
  border: none;
  
  border-radius: 4px;
  cursor: pointer;
  min-width: 80px; /* 调整按钮的最小宽度 */
   margin-left: 10px; /* 将按钮推至右侧 */
}
.favorite-item {
  /* display: flex;
   flex: 1 1 200px;
  align-items: center; */
  justify-content: space-between;
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  margin-bottom: 20px;
  padding: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

.favorite-info {
  display: flex;
  align-items: center;
}

.favorite-name {
  margin-right: 10px;
  cursor: pointer;
}

.button {
  margin-left: 10px;
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
.movie-item-small {
	width: 100%;
	border: 1px solid #e0e0e0;
	border-radius: 5px;
	margin-bottom: 20px;
	padding: 15px;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	background-color: #fff;
}
.text {
  font-size: 16px;
  white-space: nowrap; /* 防止文字换行 */
  overflow: hidden; /* 超出部分隐藏 */
  text-overflow: ellipsis; /* 超出部分显示省略号 */
}
.movie-name {
  margin: 20px;
}
.right-section {
  display: flex;
  margin-left: 80px; /* 将右侧按钮推至右边缘 */
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
