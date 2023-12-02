<template>
 <view name="referrer" content="no-referrer" >
	 <view class="container" >
		 <u-row gutter="16" justify="between">
		 		 <u-col span="4">
		 			 <u-button @click="showTag" class="tagButton" >类型筛选</u-button>
		 			 <u-overlay :show="isTagShow" @click="closeOverlay">
		 				 <view v-show="isTagShow" class="tag-contanier">
		 					 <u-tag 
		 					 v-for="tag in tags" :key="tag.id" :text="tag.text"
		 					 :type="tag.type" class="tags" @click="selectTag(tag.id)"
		 					 >
		 					 </u-tag>
		 					<u-tag text="清空筛选" @click="clearTagMask" class="tags" type="info"></u-tag>
		 				 </view>
		 			 </u-overlay>
		 		 </u-col>
		 		 <u-col span="4">
		 			 <u-input
		 			     placeholder="请输入电影名称"
		 			     border="surround"
		 			     v-model="movieName"
		 			     @change="findMovieChange"
		 			   >
					   <template slot="suffix">
					   	  <u-icon @click="clear()" name="close-circle-fill"  color="#11AC85" size="15"></u-icon>
					   	</template>
					  
					   		
					  
					  </u-input>
					
		 		 </u-col>
		 		 <u-col span="4">
		 			 <u-button @click="showRegion" class="tagButton">地区筛选</u-button>
		 			 <u-overlay :show="isRegionShow" @click="closeOverlay">
		 			  <view v-show="isRegionShow" class="tag-contanier">
		 			 		 <u-tag 
		 			 		 v-for="region in regions" :key="region.id" :text="region.text"
		 			 		 :type="region.type" class="tags" @click="selectRegion(region.id)"
		 			 		 >
		 			 		 </u-tag>
		 					 <u-tag text="清空筛选" @click="clearRegionMask" class="tags" type="info"></u-tag>
		 			  </view>
		 			  </u-overlay>
		 		 </u-col>
		 </u-row>
	 </view>
	 
	    <view class="movie-list">
     <view v-for="movie in dataList" :key="movie.id" class="movie-item">
		
       <view class="movie-info">
         <view class="movie-title">{{ movie.name }}</view>
         <view class="movie-description">{{ movie.genres }}</view>
		 <view class="movie-description">{{ movie.regions }}</view>
		 <img style="width: 30%; height: 30%;" :src='movie.cover' onerror="this.src='http://123.60.136.40:8081/MovieHunterWeb/static/movie.jpeg';this.οnerrοr=null"></img>
       </view>
       <view class="movie-actions">
         <u-button @click="favoriteHandle(movie.movieId)" type="primary"  size="small">加入收藏夹</u-button>
         <u-button @click="showMore(movie.movieId)" type="info"  size="small">查看详情</u-button>
       </view>
	   
     </view>
	 <u-icon name="arrow-up-fill" class='topback' @click="topback"></u-icon>
	 
     <view v-if="!isLoad" class="loading-text">加载中...</view>
     <view v-if="isLoad && dataList.length === 0" class="no-data-text">暂无数据</view>
	 <u-popup :show="isFavoriteShow" :closeable="true" @close="close"  mode="left"   >
		 <view class="favorites-list"> 
			 <u-list>
			 			 <u-list-item v-for="favorite in favorites" :key="favorite.favoriteId">
			 				<u-cell :title="favorite.name" @click="addMovieToFavorite(favorite.favoriteId)"></u-cell>
			 			 </u-list-item>
						 <u-list-item>
							 <u-button @click="createButton" type="primary">点击新建收藏夹</u-button>
						 </u-list-item>
						 <u-list-item v-if="isCreateShow">
							<view style="width: inherit;">
								<u-input style="width: inherit;" v-model="favoriteName" placeholder="请输入收藏夹名称"></u-input>
							</view>
								 
								 <u-button @click="createFavorite">点击创建</u-button>
							
						 </u-list-item>
			 </u-list>
		 </view>
		 
	 </u-popup>
   </view>
    <view v-if="!loadMore" bg-color="#f3f3f3">没有更多了</view >
   </view>
   
</template>

<script>
export default {
  data() {
    return {
		page:0,
		regionPage:0,
		tagPage:0,
		pageSize:7,
		namePage:0,
		token:'',
		isTagShow:false,
		isRegionShow:false,
		isLoad:false,
		tagMask:0,
		regionMask:0,
		mask:0,
		userId:'',
		favorites:[],
		isFavoriteShow:false,
		selectMovieId:0,
		movieName:'',
		favoriteName:'默认',
		isCreateShow:false,
		loadMore:true,
		 tags:
  [
    { id: 0, text: '舞台艺术', type: 'info', mask: 1 },
    { id: 1, text: '科幻', type: 'info', mask: 2 },
    { id: 2, text: '古装', type: 'info', mask: 4 },
    { id: 3, text: '儿童', type: 'info', mask: 8 },
    { id: 4, text: '灾难', type: 'info', mask: 16 },
    { id: 5, text: '传记', type: 'info', mask: 32 },
    { id: 6, text: '歌舞', type: 'info', mask: 64 },
    { id: 7, text: '动作', type: 'info', mask: 128 },
    { id: 8, text: '犯罪', type: 'info', mask: 256 },
    { id: 9, text: '惊悚', type: 'info', mask: 512 },
    { id: 10, text: '家庭', type: 'info', mask: 1024 },
    { id: 11, text: '黑色电影', type: 'info', mask: 2048 },
    { id: 12, text: '动画', type: 'info', mask: 4096 },
    { id: 13, text: '武侠', type: 'info', mask: 8192 },
    { id: 14, text: '冒险', type: 'info', mask: 16384 },
    { id: 15, text: '脱口秀', type: 'info', mask: 32768 },
    { id: 16, text: '历史', type: 'info', mask: 65536 },
    { id: 17, text: '短片', type: 'info', mask: 131072 },
    { id: 18, text: '音乐', type: 'info', mask: 262144 },
    { id: 19, text: '剧情', type: 'info', mask: 524288 },
    { id: 20, text: '悬疑', type: 'info', mask: 1048576 },
    { id: 21, text: '战争', type: 'info', mask: 2097152 },
    { id: 22, text: '恐怖', type: 'info', mask: 4194304 },
    { id: 23, text: '奇幻', type: 'info', mask: 8388608 },
    { id: 24, text: '喜剧', type: 'info', mask: 16777216 },
    { id: 25, text: '运动', type: 'info', mask: 33554432 },
    { id: 26, text: '爱情', type: 'info', mask: 67108864 },
    { id: 27, text: '西部', type: 'info', mask: 134217728 },
    { id: 28, text: '其他', type: 'info', mask: 2147483648 }
  ],
  regions:[{ id: 0, text: '中国大陆', type: 'info', mask: 1 },
  { id: 1, text: '中国台湾', type: 'info', mask: 2 },
  { id: 2, text: '中国香港', type: 'info', mask: 4 },
  { id: 3, text: '英国', type: 'info', mask: 8 },
  { id: 4, text: '法国', type: 'info', mask: 16 },
  { id: 5, text: '德国', type: 'info', mask: 32 },
  { id: 6, text: '意大利', type: 'info', mask: 64 },
  { id: 7, text: '西班牙', type: 'info', mask: 128 },
  { id: 8, text: '加拿大', type: 'info', mask: 256 },
  { id: 9, text: '阿根廷', type: 'info', mask: 512 },
  { id: 10, text: '丹麦', type: 'info', mask: 1024 },
  { id: 11, text: '波兰', type: 'info', mask: 2048 },
  { id: 12, text: '韩国', type: 'info', mask: 4096 },
  { id: 13, text: '马来西亚', type: 'info', mask: 8192 },
  { id: 14, text: '荷兰', type: 'info', mask: 16384 },
  { id: 15, text: '挪威', type: 'info', mask: 32768 },
  { id: 16, text: '比利时', type: 'info', mask: 65536 },
  { id: 17, text: '墨西哥', type: 'info', mask: 131072 },
  { id: 18, text: '俄罗斯', type: 'info', mask: 262144 },
  { id: 19, text: '瑞典', type: 'info', mask: 524288 },
  { id: 20, text: '巴西', type: 'info', mask: 1048576 },
  { id: 21, text: '泰国', type: 'info', mask: 2097152 },
  { id: 22, text: '澳大利亚', type: 'info', mask: 4194304 },
  { id: 23, text: '印度', type: 'info', mask: 8388608 },
  { id: 24, text: '日本', type: 'info', mask: 16777216 },
  { id: 25, text: '匈牙利', type: 'info', mask: 33554432 },
  { id: 26, text: '美国', type: 'info', mask: 67108864 },
  { id: 27, text: '以色列', type: 'info', mask: 134217728 },
  { id: 28, text: '芬兰', type: 'info', mask: 0x1<<28 },
  { id: 29, text: '伊朗', type: 'info', mask: 536870912 },
  { id: 30, text: '爱尔兰', type: 'info', mask: 1073741824 },
  { id: 31, text: '其他', type: 'info', mask: 2147483648 }],
      dataList: [] // 从后端接收到的电影数据将会存储在这里
    };
  },
 
  mounted() {
	  var that = this
	  var userId = ''
	  var value = uni.getStorageSync('token');
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
	  uni.request({
	  		url:this.$BASE_URL.BASE_URL+'user/parseToken/'+that.token,
	  		method:'GET',
	  		success(res) {
	  			if(res.data.code==200)
	  			{
	  				console.log(res)
					console.log(res.data.data.userId)
	  				that.userId = res.data.data.userId
	  				
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
	  	}
	  })
	  
	 
    console.log(that.token);
    // 从后端获取数据的逻辑，比如使用axios库进行异步请求
	that.loadData();
		
},
	
 
  onReachBottom(){
     // 触底时加载更多数据
        if(this.loadMore)
		{
			 this.loadData();
		}
		
       
		
  },
  methods:{
	  topback(){
		  uni.pageScrollTo({
		  				scrollTop:0,   // 滚动到页面的目标位置  这个是滚动到顶部, 0 
		  				duration:300  // 滚动动画的时长
		  			})
	  },
	  createButton()
	  {
		  this.isCreateShow = true
	  },
	  close() {
		  this.isCreateShow = false
	  	this.isFavoriteShow = false
	  },
	  findMovieChange()
	  {
		  console.log('change')
		  var that = this
		  if(this.movieName=='')
		  {	
			  this.page = 0
			  this.dataList = []
			  this.updateData()
			  return
		  }else{
			  console.log(this.movieName)
			  this.namePage = 1
			  this.clearRegionMask()
			  this.clearTagMask()
			  uni.request({
			  	url:this.$BASE_URL.BASE_URL+'movie/findMoviesByName?name='+that.movieName+'&pageNum='+that.namePage+'&pageSize='+that.pageSize,
			  			method:"GET",
			  			header:{token:that.token},
			  			success(res) {
			  				if(res.data.code==200)
			  				{
			  					that.dataList = res.data.data.records
								if(that.namePage==res.data.data.pages){
									that.loadMore = false
								}
			  				}
			  			},
			  			complete(res) {
			  				console.log(res)
			  			}
			  			
			  			
			  })
		  }
		  
		  
	  },
	  closeOverlay()
	  {
		  this.isTagShow=false
		  this.isRegionShow=false
	  },
	  selectTag(index)
	  {
		  
		  this.tagPage = 0
		  if(this.tags[index].type=="info")
		  {
			  this.tags[index].type="primary"
			  
		  }else{
			  
			  this.tags[index].type="info"
		  }
		  this.computeMask('tag');
		  
		  if(this.tagMask==0)
		  {
		  			 this.updateData('normal')
		  }else{
		  			  this.updateData('tag')
		  }
		
	  },
	  selectRegion(index)
	  {
		  this.regionPage = 0
		  if(this.regions[index].type=="info")
		  {
		  			  this.regions[index].type="primary"
		  			 
		  }else{
		  			  this.regions[index].type="info"
		  }
		  this.computeMask('region');
		 if(this.regionMask==0)
		 {
			 this.updateData('normal')
		 }else{
			  this.updateData('region')
		 }
		 
	  },
	  clear(){
		  this.movieName = ''
		  this.loadMore = true
		  
		  this.updateData()
	  },
	  updateData(){
		 
		  var that=this;
		  console.log(this.page)
	      var url;
		  if(this.tagMask!=0){
			  url = this.$BASE_URL.BASE_URL+'movie/findMoviesByGenresMask/'+that.tagMask+'?pageNum=1'+'&pageSize='+that.pageSize
			  this.tagPage = 1
		  }else if(this.regionMask!=0){
			  url = this.$BASE_URL.BASE_URL+'movie/findMoviesByRegionsMask/'+that.regionMask+'?pageNum=1'+'&pageSize='+that.pageSize
			  this.regionPage = 1
		  }else{
			  url = this.$BASE_URL.BASE_URL+'movie/findMoviesByPages/1'+'?pageSize=7'
			  this.page = 1
		  }
		  uni.request({
		  	url:url,
			method:'GET',
			header:{
				token:that.token
				},
				success(res) {
					if(res.data.code==200)
					{
						that.isLoad=true;
						console.log(res)
						console.log("请求成功")
						console.log(res)
						that.dataList =  res.data.data.records;
					}
					else if(res.data.code==401){
						console.log(res)
						uni.redirectTo({
						 url:'/pages/LoginByEmaiL/LoginByEmaiL'
						})
					}
				},
				fail(res) {
					console.log(res)
					uni.showToast({
						title:"网络出错了",
						icon:'error'
					})
				}
			
			
		  })
	  },
	  computeMask(e)
	  {
		  if(e=='tag')
		  {
			  for (var i = 0; i < this.tags.length; i++) {
			  	if(this.tags[i].type=="primary")
			  			{
			  				this.tagMask = this.tags[i].mask|this.tagMask
			  			}
				else{
					this.tagMask = (~this.tags[i].mask)&this.tagMask
				}
			  }
		  }else{
			  for (var i = 0; i < this.regions.length; i++) {
			  	if(this.regions[i].type=="primary")
			  			{
			  				this.regionMask = this.regions[i].mask|this.regionMask
			  			}
						else{
							this.regionMask = (~this.regions[i].mask)&this.regionMask
						}
		  }
		  }
		  console.log(this.regionMask)
		  console.log(this.tagMask)
	  },
	  showTag()
	  {
		  this.movieName = ''
		  this.clearRegionMask()
		  this.isTagShow=!this.isTagShow
		  if(this.dataList.length==0){this.loadData()}
	  },
	  showRegion(){
		   this.movieName = ''
		 this.clearTagMask()
		  console.log("showregion")
		  this.isRegionShow=!this.isRegionShow
		  if(this.dataList.length==0){this.loadData()}
	  },
	 handleButtonClick(e) {
	        console.log('click left button', e);
	      },
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
		this.isFavoriteShow=false
		this.updateFavorite()
		
		
		
	},
	updateFavorite(){
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
		this.showFavorite()
	},
	  favoriteHandle(e)
	  {
		  var that = this
		  console.log(that.token)
		 uni.request({
		 	url:this.$BASE_URL.BASE_URL+'favorite/findFavoritesByCurUser/',
		 		method:'GET',
		 		header:{
		 			token:that.token
		 			},
		 			success(res) {
		 				console.log(res)
		 				
							that.favorites = res.data.data
							that.showFavorite()
							that.selectMovieId = e
							
						
		 				
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
	  showFavorite()
	  {
	  	this.isFavoriteShow = true
	  },
	  addMovieToFavorite(e)
	  {
		  var that = this
		  console.log(e)
		  uni.request({
		  	url:this.$BASE_URL.BASE_URL + 'favorite/insertMovieToFavorite',
			method:'PUT',
			header:{token:that.token},
			data:{
				favoriteId:e,
				movieId:that.selectMovieId
			},
			success(res) {
				if(res.data.code==200)
				{
					uni.showToast({
						title:"添加成功",
					})
					
					
				}else{
					console.log(res)
					uni.showToast({
						title:res.data.message,
						icon:'none'
					})
				}
				
			},
			fail(res) {
							console.log(res)
							uni.showToast({
								title:"网络出错了",
								icon:'error'
							})
						}
		  })
	  },
	  showMore(e)
	  {		uni.navigateTo({
			url:'/pages/MovieInfo/MovieInfo?movieId='+e
			
	  }),
		   console.log(e)
	  },
	  clearTagMask()
	  {
		  for (var i = 0; i < this.tags.length; i++) {
		  	this.tags[i].type='info'
		  }
		  this.closeOverlay()
		  this.dataList=[]
		 this.tagMask = 0
		 this.tagPage = 0
		 this.page = 0
		  this.updateData()
		 
	  },
	  clearRegionMask()
	  {
	  		  for (var i = 0; i < this.regions.length; i++) {
	  		  	this.regions[i].type='info'
	  		  }
	  		  this.closeOverlay()
	  		  this.dataList=[]
			  this.regionPage = 0
			  this.regionMask = 0
			  this.page = 0
			   this.updateData()
			 
	  		  
	  },
	   loadData() {
		   uni.showLoading({
		   	title:'加载中'
		   })
		   console.log(this.token)
		   console.log(this.tagMask)
		   console.log(this.regionMask)
		   var that=this
		   var url = ''
		   var page = 0
		   if(this.tagMask!=0)
		   {
			  this.tagPage++
			   url  = this.$BASE_URL.BASE_URL+'movie/findMoviesByGenresMask/'+that.tagMask+'?pageNum='+that.tagPage+'&pageSize='+that.pageSize
			   page = this.tagPage
			    
		   }
		   else if(this.regionMask!=0)
		   {
			   this.regionPage++
			   url = this.$BASE_URL.BASE_URL+'movie/findMoviesByRegionsMask/'+that.regionMask+'?pageNum='+that.regionPage+'&pageSize='+that.pageSize
			   page = this.regionPage
			   
		   }
		   else if(this.movieName!=''){
			  this.namePage++
			   console.log(this.namePage)
			   console.log(this.movieName)
			   url = this.$BASE_URL.BASE_URL+'movie/findMoviesByName?name='+that.movieName+'&pageNum='+that.namePage+'&pageSize='+that.pageSize
			   console.log(url)
			   page = this.namePage
			    
			   
		   }
		   else{
			   console.log(this.page)
			   this.page++
			   url = this.$BASE_URL.BASE_URL+'movie/findMoviesByPages/'+that.page+'?pageSize=7'
			   page = this.page
			    
		   }
	        uni.request({
	        		url:url,
	        		method:'GET',
	        		header:{
	        			token:that.token
	        			},
	        		
	        		success(res) {
	        			if(res.data.code==200)
	        			{
							that.isLoad=true;
	        				
	        				console.log("请求成功")
							console.log(res)
	        				that.dataList =  that.dataList.concat(res.data.data.records);
							console.log(page)
							if(res.data.data.pages == page || res.data.data.pages < page)
							{
								that.loadMore = false
							}else{
								that.loadMore = true
							}
	        			}
	        			else if(res.data.code==401){
							console.log(res)
	        				uni.redirectTo({
	        				 url:'/pages/LoginByEmaiL/LoginByEmaiL'
	        				})
	        			}
						uni.hideLoading()
	        		},
	        		fail(res) {
	        			console.log(res)
	        			uni.showToast({
	        				title:"网络出错了",
	        				icon:'fail'
	        			})
	        		},
					complete(res) {
						console.log(res)
					}
			})
  }
  }
};
</script>
<style>

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
.movie-info {
  margin-bottom: 10px;
}
.movie-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 5px;
}
.movie-actions {
  display: flex;
  justify-content: space-between;
}
.loading-text, .no-data-text {
  margin-top: 20px;
  font-size: 16px;
  color: #666;
}
.filter-page {
  padding: 20px;
}
.filter-section {
  margin-bottom: 20px;
}
.filter-item {
  display: flex;
  align-items: center;
}
.filter-label {
  width: 80px;
}
.filter-value {
  flex: 1;
  padding: 8px;
  background-color: #f5f5f5;
  border-radius: 5px;
}
.tags{
	
	overflow-wrap:break-word;
	width: 80px;
	padding: 8px;
	
	
	
}
.tag-contanier{
display:flex;
flex-flow:row wrap
	
}
.tagButton{
	width: 100px;
	align-items: center;
}
.container{
	margin-top: 15px;
}
.favorites-list{
	margin-top: 50px;
}
.topback{
	 position: fixed;
	  right: 2rem;
	  bottom: 2rem;
	  size:25rpx;
	  color: coral;
}
</style>