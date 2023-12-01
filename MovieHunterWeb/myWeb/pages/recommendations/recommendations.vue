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
		 
	<view class="search-main">
		 <div class="search-view">
			<u-col span="12">
						 <u-input
						     placeholder="搜索电影"
						     v-model="movieName"
						     @change="findMovieChange"
							 class="search"
						   ></u-input>
			</u-col>
		</div>
	</view>
	<view class="main">
	 <u-row gutter="16" justify="between">
		 <u-col span="1">
			 <u-button v-show="tagMask==0" @click="showTag" class="tagButton">按类型筛选</u-button>
			 <u-button v-show="tagMask!=0" @click="showTag" class="tagButton" style="background-color:aliceblue">按类型筛选</u-button>
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
		 <u-col span="1">
			 <u-button v-show="regionMask==0" @click="showRegion" class="tagButton">按地区筛选</u-button>
			 <u-button v-show="regionMask!=0" @click="showRegion" class="tagButton" style="background-color:aliceblue">按地区筛选</u-button>
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
		 <u-col span="1">
		 		<u-button v-show="recommendMask==0" @click="setTag" class="tagButton">个性化推荐</u-button>
				<u-button v-show="recommendMask!=0" @click="setTag" class="tagButton" style="background-color:aliceblue">个性化推荐</u-button>
		 </u-col>
	 </u-row>
	</view>
	
	<view class="main">
		  <div>
		      <div v-if="isFavoriteShow" class="overlay">
				<div class="sub-window-big">
					<div @click="closeSubWindow" class="close-button">&times;</div>
					<div class="scrollable-list">
					<div @click="createButton" class="create-button">新建收藏夹</div>
						<u-list class="favourite-list">
							 <u-list-item v-for="favorite in favorites" :key="favorite.favoriteId" >
								<u-cell :title="favorite.name" @click="addMovieToFavorite(favorite.favoriteId)" class="cell-button"></u-cell>
							 </u-list-item>
						</u-list>
					</div>
				</div>
			</div>
		        <view v-if="isCreateShow" class="overlay">
					<div class="sub-window-create">
						<div @click="closeSubWindow_create" class="close-button">&times;</div>
						<u-input  v-model="favoriteName" placeholder="请输入收藏夹名称"></u-input>
						<u-button @click="createFavorite">点击创建</u-button>
						<view v-if="isErrorShow" style="color: red;">{{errorMessage}}</view>
					</div>
		        </view>
				
		      
		    </div>
	<view class="movie-list">
		<view v-for="movie in dataList" :key="movie.id" class="movie-item">
			<view class="movie-item-top">
			<view class="movie-main"/>
				<img class="movie-poster" :src="movie.cover" alt="无海报" onerror="this.src='http://123.60.136.40:8081/MovieHunterWeb/static/movie.jpeg';this.οnerrοr=null"></img>
			   <view class="movie-info">
				 <view class="movie-title">{{ movie.name }}</view>
				 <view class="movie-year">{{ movie.year }}</view>
				 <view class="movie-regions">{{ movie.regions }}</view>
				 <view class="movie-genres">{{ movie.genres }}</view>
				 <view class="movie-score">
				 <u-rate class="centered-rate" :value="movie.score/2" :active-color="'#FFD700'" :inactive-color="'#CCCCCC'" :size="20" readonly></u-rate>
				 </view>
			   </view>
			</view>
		   <view class="movie-actions">
			 <u-button @click="favoriteHandle({id: movie.movieId, mask: movie.genresMask})" type="primary"  size="small">加入收藏夹</u-button>
			 <u-button @click="showMore({id: movie.movieId, mask: movie.genresMask})" type="info"  size="small">查看详情</u-button>
		   </view>
		 </view>
		 <view v-if="!isLoad" class="loading-text">加载中...</view>
		 <view v-if="isLoad && dataList.length === 0" class="no-data-text">暂无数据</view>
		 <u-button v-show="isLoad && loadMore" @click="loadData()" class="tagButton">加载更多</u-button>
	   </view>
	</view>
	
	</view>
	
</template>

<script>
export default {
  data() {
    return {
		page:0,
		pageSize:10,
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
		isErrorShow:false,
		errorMessage:"",
		clickHistory:[],
		recommendMask:0,
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
  { id: 28, text: '芬兰', type: 'info', mask: 1<<28 },
  { id: 29, text: '伊朗', type: 'info', mask: 536870912 },
  { id: 30, text: '爱尔兰', type: 'info', mask: 1073741824 },
  { id: 31, text: '其他', type: 'info', mask: 2147483648 }],
      dataList: [] // 从后端接收到的电影数据将会存储在这里
    };
  },
  
  onReachBottom(){
	 // 触底时加载更多数据
	    if(this.loadMore)
	 		{
	 			 // this.page++;
	 			 console.log(this.page);
	 			 this.loadData();
	 		}
        
  },
onLoad() {
	  var that = this
	  var userId = ''
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
	  })
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
	this.loadData();
		
},
	
  methods:{
	  handleClick(e){
	  	uni.redirectTo({
	  		url:'/pages/'+e
	  	})
	  },
	  isMaskNull(e) {
		  return e===0
	  },
	  updateHistory(e) {
			var id = e['id']
			var t = new Date().toLocaleTimeString();
		  
			// 查找是否存在相同ID的记录
			var existingItemIndex = this.clickHistory.findIndex(function(item) {
			  return item.id === id;
			});
			// 如果找到相同ID的记录，只更新时间
			if (existingItemIndex !== -1) {
			  this.clickHistory[existingItemIndex].t = t;
			}
			// 如果不重复且不满三个，直接添加新记录
			else if (this.clickHistory.length < 3) {
			  this.clickHistory.push({ id: id, t: t, mask: e['mask']});
			}
			// 如果不重复且满三个，替换时间最早的记录
			else {
				var earliestItemIndex = 0;
				var earliestTime = this.clickHistory[0].t;
	  
				for (var i = 1; i < this.clickHistory.length; i++) {
					if (this.clickHistory[i].t < earliestTime) {
					earliestItemIndex = i;
					earliestTime = this.clickHistory[i].t;
					}
				}
				this.clickHistory[earliestItemIndex] = { id: id, t: t, mask: e['mask']};
			}
	  },
	  setTag() {
		this.movieName=''
		this.recommendMask = 0
		this.regionMask = 0
		this.tagMask = 0
		for (var i = 0; i < this.tags.length; i++) {
			this.tags[i].type='info'
		}
		for (var i = 0; i < this.regions.length; i++) {
			this.regions[i].type='info'
		}
		for (var i = 0; i < this.clickHistory.length; i++) {
			this.recommendMask = this.recommendMask | this.clickHistory[i]['mask']
		}
		
		this.updateData()
	  },
	  showSubWindow() {
		this.isFavoriteShow = true;
		// 禁用背后的其他控件
		document.body.style.overflow = 'hidden';
	  },
	  closeSubWindow() {
		this.isFavoriteShow = false;
		// 启用背后的其他控件
		document.body.style.overflow = 'auto';
	  },
	  createButton()
	  {
		  this.isFavoriteShow = false;
	  		this.isCreateShow = true
			this.isErrorShow=false
	  },
	  closeSubWindow_create(){
		  this.isFavoriteShow = true;
		  this.isCreateShow = false
		  this.isErrorShow=false
	  },
	  close() {  
			this.isFavoriteShow = false
	  },
	  findMovieChange()
	  {
			 console.log(this.movieName)
			 this.updateData()
	  },
	  closeOverlay()
	  {
		  this.isTagShow=false
		  this.isRegionShow=false
	  },
	  selectTag(index)
	  {
		  console.log(index)
		  if(this.tags[index].type=="info")
		  {
			  this.tags[index].type="primary"
			 
		  }else{
			  this.tags[index].type="info"
		  }
		  this.recommendMask = 0
		  this.computeMask('tag');
		  this.updateData('tag');
		
	  },
	  selectRegion(index)
	  {
		  this.recommendMask = 0
		  if(this.regions[index].type=="info")
		  {
		  			  this.regions[index].type="primary"
		  			 
		  }else{
		  			  this.regions[index].type="info"
		  }
		  this.computeMask('region');
		  this.updateData('region')
	  },
	  updateData(e){
		  var that=this;
		  // var url;
		  that.page = 0;
		  that.dataList=[]
		  if(e=='tag'){
			  that.regionMask = 0
			  for (var i = 0; i < this.regions.length; i++) {
			  	this.regions[i].type='info'
			  }
		  }else if(e=='region'){
			  that.tagMask = 0
			  for (var i = 0; i < this.tags.length; i++) {
			  	this.tags[i].type='info'
			  }
		  }else{
			  that.regionMask = 0
			  for (var i = 0; i < this.regions.length; i++) {
			  	this.regions[i].type='info'
			  }
			  that.tagMask = 0
			  for (var i = 0; i < this.tags.length; i++) {
			  	this.tags[i].type='info'
			  }
		  }
		  this.loadData();
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
				else
						{
							this.tagMask = (~this.tags[i].mask)&this.tagMask
						}
			  }
		  }else{
			  for (var i = 0; i < this.regions.length; i++) {
			  	if(this.regions[i].type=="primary")
			  			{
			  				this.regionMask = this.regions[i].mask|this.regionMask
			  			}
				else
						{
							this.regionMask = (~this.regions[i].mask)&this.regionMask
						}
		  }
		  }
		  console.log(this.regionMask)
		  console.log(this.tagMask)
	  },
	  showTag()
	  {
		  this.isTagShow=!this.isTagShow
		  if(this.dataList.length==0){this.loadData()}
	  },
	  showRegion(){
		  console.log("showregion")
		  this.isRegionShow=!this.isRegionShow
		  if(this.dataList.length==0){this.loadData()}
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
		this.showSubWindow()
	  }, 
	  showError(e) {
		  this.isErrorShow = true
		  this.errorMessage = e
	  },
	  createFavorite(e)
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
					that.isFavoriteShow=false
					that.updateFavorite()
				}
	  		},
	  		fail(res) {
				that.showError("网络出错了")
	  		},
			
	  	})
	  	
	  	
	  	
	  	
	  },
	    favoriteHandle(e)
	    {
			this.updateHistory(e)
	  	  var that = this
	  	 uni.request({
	  	 	url: this.$BASE_URL.BASE_URL + 'favorite/findFavoritesByCurUser/',
	  	 		method:'GET',
	  	 		header:{
	  	 			token:that.token
	  	 			},
	  	 			success(res) {
	  	 				console.log(res)
	  	 				if(res.data.data.length==0)
	  	 				{
	  	 					// that.createFavorite(e['id'])
							that.showSubWindow()
							that.selectMovieId = e['id']
	  	 				}
	  					else{
	  						that.favorites = res.data.data
	  						that.showSubWindow()
	  						that.selectMovieId = e['id']
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
					that.closeSubWindow()
	  				uni.showToast({
	  					title:"添加成功",
						icon:"success"
	  				})
	  			}else{
	  				that.closeSubWindow()
	  				uni.showToast({
	  					title:"重复添加",
	  					icon:"error"
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
	  {		
		this.updateHistory(e)
		uni.navigateTo({
		url:'/pages/MovieInfo/MovieInfo?movieId='+e['id']
	  }),
		   console.log(e)
	  },
	  clearTagMask()
	  {
		  for (var i = 0; i < this.tags.length; i++) {
		  	this.tags[i].type='info'
		  }
		  this.closeOverlay()
		  this.tagMask = 0
		  this.recommendMask = 0
		  this.updateData()
	  },
	  clearRegionMask()
	  {
		  for (var i = 0; i < this.regions.length; i++) {
			this.regions[i].type='info'
		  }
		  this.closeOverlay()
		  this.regionMask = 0
		  this.recommendMask = 0
		  this.updateData()
	  },
	   loadData() {
		   if(this.page==0){
			   this.loadMore=true
			   this.dataList=[]
		   }
		   this.page++
		   if(!this.loadMore){
			   uni.showToast({
			   	title:"已经到底!",
			   })
			   return
		   }
		   uni.showLoading({
		   	title:'加载中'
		   })
		   var that=this
		   var myurl
		   if(that.movieName!=''){
		   		myurl = this.$BASE_URL.BASE_URL+'movie/findMoviesByName?name='+that.movieName+'&pageNum='+that.page+'&pageSize='+that.pageSize
		   }else if(that.recommendMask!=0){
			    myurl = this.$BASE_URL.BASE_URL+'movie/findMoviesByGenresMask/'+that.recommendMask+'?pageNum='+that.page+'&pageSize='+that.pageSize
		   }else if(that.tagMask!=0){
				myurl = this.$BASE_URL.BASE_URL+'movie/findMoviesByGenresMask/'+that.tagMask+'?pageNum='+that.page+'&pageSize='+that.pageSize
		   }else if(that.regionMask != 0){
				myurl = this.$BASE_URL.BASE_URL+'movie/findMoviesByRegionsMask/'+that.regionMask+'?pageNum='+that.page+'&pageSize='+that.pageSize
		  }
		  else{
			  myurl = this.$BASE_URL.BASE_URL+'movie/findMoviesByPages/'+that.page+'?pageSize='+that.pageSize
		  }
	        uni.request({
	        		url:myurl,
	        		method:'GET',
	        		header:{
	        			token:that.token
	        			},
	        		
	        		success(res) {
	        			if(res.data.code==200)
	        			{
							uni.hideLoading()
							that.isLoad=true;
	        				console.log(that.page)
	        				console.log("请求成功")
							console.log(res.data.data.pages)
							console.log(that.dataList)
	        				
							if(res.data.data.pages < that.page )
							{
								that.loadMore = false
								if(that.page!=1){
									uni.showToast({
										title:"已经到底",
										icon:'error'
									})
								}
							}else if(that.loadMore){
								that.loadMore = res.data.data.pages > that.page
								if(that.page==1){
									that.dataList =  res.data.data.records;
								}else{
									that.dataList =  that.dataList.concat(res.data.data.records);
								}
								if(!that.loadMore){
									if(that.page!=1){
										uni.showToast({
											title:"已经到底",
											icon:'error'
										})
									}
								}
							}
	        			}
	        			else if(res.data.code==401){
							console.log(res)
	        				uni.redirectTo({
	        					url:'/pages/Login/LoginByEmail'
	        				})
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
  }
  }
};
</script>
<style>
.scrollable-list {
  overflow-y: auto;
  height: 90%;
}	
.scrollable-list {
	margin-top: 40px;
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

.create-button {
  position: absolute;
  top:17px;
  left: 12px;
  color: #999;
  cursor: pointer;
}
.create-button:hover {
  color: #333;
}

.cell-button {
  padding: 8px;
  cursor: pointer;
}

.cell-button:hover {
  background-color: #f5f5f5;
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

.sub-window-big {
  position: relative;
 width: 400px;
  height: 500px;
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
.main {
	display: flex;
	justify-content: center;
	background-color: #f5f5f5;
}
.search-main {
	display: flex;
	justify-content: center;
	
	background-color: #f5f5f5;
}
.search-view {
	margin-top: 10pt;
	width: 30%;
	border-radius: 10pt;
	border:2px solid #ccc;
}
.search {
	background-color: #ffffff;
	border-radius: 10pt;
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
.movie-poster {
	margin-right: 10pt;
	margin-bottom: 7pt;
	width: 100pt;
	height: 120pt;
}
.movie-list {
   display: flex;
	flex-wrap: wrap;
	justify-content: space-between;
	width: 50%;
	/* background-color: #f9fff9; */
}
.movie-item {
   width: calc(50% - 30px); /* 每列宽度为一半，减去间距 */
    margin-bottom: 20px;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 8px;
	height: 200px;
	background-color: #f9feff;
}
.movie-item-top {
	display: flex;
	justify-content: start;
}
.movie-info {
  margin-bottom: 10px;
}
.movie-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 5px;
}
.movie-description {
  color: #555;
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
	padding: 15px;	
}
.tag-contanier{
display:flex;
flex-flow:row wrap
	
}
.tagButton{
	width: 100px;
	align-items: center;
}

.centered-rate {
  display: block;
  margin: 0 auto; /* 将评分的星星居中显示 */
}
.movie-score {
	/* display: flex;
	justify-content: start; */
}
</style>