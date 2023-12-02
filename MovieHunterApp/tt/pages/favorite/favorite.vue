<template>
  <view >
	
    <view class="favorites-list">
      <view v-for="favorite in favorites" :key="favorite.favoriteId" class="favorite-item">
        <view class="container" >
          <view class="left-section" @click="showFavoriteMovies(favorite.favoriteId)"><text class="text" >{{ favorite.name }}</text></view>
		  <view class="right-section">
			   <u-button class="edit-button" @click="editItem(favorite.favoriteId)" text="编辑"></u-button>
			  <u-button text="移除" type="warning" class="remove-button" @click="deleteFavorite(favorite.favoriteId)"></u-button> 
		  </view>
          
        </view>
      </view>
    </view>
<button class="favorite-button" @click="openPopup"  size="mini">新建收藏夹</button>
    <u-popup :show="isMoviesShow" :closeable="true" @close="closeMoviesShow()" closeIconPos="top-left" :safeAreaInsetTop="true">
      <view class="movie-list">
        <view v-for="movie in movies" :key="movie.movieId" class="movie-item">
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
	  
    </u-popup>
	
		<u-popup  :show="showPopup" class="popup" :closeable="true" @close="closePopup">
	 <div>
	      <div class="popup-content">
	        <input type="text" v-model="inputText" placeholder="请输入新名称">
	        <button class="submit-button" @click="submitText">提交</button>
	      </div>
	    </div>
	</u-popup>
	<u-popup :show="showChange" class="popup" :closeable="true" @close="closeShowChange()">
		<view v-for="favorite in favorites" :key="favorite.favoriteId" >
		    <view class="container" >
		      <view class="left-section" @click="showFavoriteMovies(favorite.favoriteId)"><text class="text" >{{ favorite.name }}</text></view>
			  <view class="right-section">
				   <u-button class="edit-button" @click="confirmChange(favorite.favoriteId)" text="确认移动"></u-button>
				  
			  </view>
		      
		    </view>
		  </view>
		
	</u-popup>
	<u-popup :show="isCreateShow" :closeable="true" @close="closeCreate()">
							<view style="width: inherit;">
									<u-input style="width: inherit;" v-model="favoriteName" placeholder="请输入收藏夹名称"></u-input>
								</view>
									 
									 <u-button @click="createFavorite">点击创建</u-button>
								
	</u-popup>
  </view>
  
</template>

<script>
export default {
  data() {
    return {
		favoriteName:'',
      favorites: [],
      userId: '',
      movies: [],
	  showChange:false,
	  selectMovieId:0,
      isMoviesShow: false,
      token: '',
	  isCreateShow:false,
      selectFavoriteId: 0,
	  showPopup: false,
	inputText: '',
	
    };
  },
  methods: {
	  createFavorite()
	  {
	  	var that = this
	  	
	  	uni.request({
	  		url:this.$BASE_URL.BASE_URL + 'favorite/insertFavorite',
	  		method:'PUT',
	  		header:{token:that.token},
	  		data:{
	  			userId:that.userId,
	  			name:that.favoriteName
	  		},
	  		success(res) {
	  			if(res.data.code==200)
	  			{
	  				uni.showToast({
	  					title:'创建成功'
	  				})
					that.showFavorite();
	  			}
	  			else{
	  				uni.showToast({
	  					title:res.data.message,
	  					icon:'none'
	  				})
	  			}
	  			console.log(res)
	  		},
	  		fail(res) {
	  			console.log(res)
	  			uni.showToast({
	  				title:"网络出错了",
	  				icon:'fail'
	  			})
	  		}
	  		
	  		
	  	})
	  	this.favoriteName = ''
	  	this.isCreateShow=false
	  	
	  	
	  	
	  	
	  },
	  openPopup(){
		  this.isCreateShow = true
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
					uni.showToast({
						title:'移动成功'
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
			  console.log(res);
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
					uni.showToast({
						title:'修改成功'
					})
				}else{
					uni.showToast({
						title:res.data.message,
						icon:'none'
					})
				}
			},
			complete(res) {
				console.log(res)
				that.showFavorite();
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
                  uni.showToast({
                    title: '删除成功'
                  });
                }
                
              },
              complete(res) {
                console.log(res);
				 that.showFavorite();
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
              	title:'暂无电影',
              	icon:'none'
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
	closeCreate(){
		this.isCreateShow = false
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
            uni.showToast({
              title: '移除成功'
            });
          } else {
			  uni.showToast({
			  	title:res.data.message,
			  	icon:'none'
			  })
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
	  this.isMoviesShow = false
      this.showFavoriteMovies(this.selectFavoriteId);
    }
  },
  onLoad(e) {
    var that = this;
   const value = uni.getStorageSync('token');
   if(value)
   {
   		  that.token=value
   		  console.log(that.token)
   }else{
   		  uni.showToast({
   		  	icon:Error,
   		  	title:"登录校验失败，请重新登录"
   		  })
   		  uni.redirectTo({
   		  	url:'/pages/LoginByEmaiL/LoginByEmaiL'
   		  })
   }
    this.userId = e.userId;
    this.showFavorite();
  }
};
</script>

<style>
.container {
  display: flex;
  align-items: center;
  padding: 10px;
 
}
.left-section {
  flex: 1;
  
}

.favorites-list {
 display: flex;
   flex-wrap: wrap;
   gap: 10px;
}
.remove-button {
  margin-right: 10px;
  
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
  display: flex;
   flex: 1 1 200px;
  align-items: center;
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
.favorite-button {
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  position: fixed;
  bottom:5%;
  left:30%;
  right: 30%;
  
}
.favorite-button:hover {
  background-color: #0056b3;
}
</style>