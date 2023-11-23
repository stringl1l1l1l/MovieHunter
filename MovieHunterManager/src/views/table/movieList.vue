<template>
    <div class="app-container">
        <div class="filter-container">
            <el-row>
                <el-col :span="15">
                    <el-button class="filter-item" style="margin-left: 10px; margin-bottom: 20px" type="primary"
                        icon="el-icon-edit" @click="handleCreate">
                        Add
                    </el-button>
                </el-col>

                <el-col :span="6" style="float:right">
                    <el-input clearable placeholder="请输入搜索内容" prefix-icon="el-icon-search" v-model="keyword" @input="fetchData(1)" />
                </el-col>
            </el-row>
        </div>

        <el-table v-loading="listLoading" :data="list" element-loading-text="加载中……" border style="width: 100%;" fit
            highlight-current-row>
            <el-table-column align="center" label="序号" min-width="50">
                <template slot-scope="scope">
                    {{ scope.$index }}
                </template>
            </el-table-column>
            <el-table-column label="电影ID" align="center">
                <template slot-scope="scope">
                    {{ scope.row.movieId }}
                </template>
            </el-table-column>
            <el-table-column label="电影名称" align="center">
                <template slot-scope="scope">
                    <span>{{ scope.row.name }}</span>
                </template>
            </el-table-column>
            <el-table-column label="电影别名" align="center">
                <template slot-scope="scope">
                    {{ scope.row.alias }}
                </template>
            </el-table-column>

            <el-table-column label="海报" min-width="100" align="center">
                <template slot-scope="scope">
                    {{ scope.row.cover }}
                </template>
            </el-table-column>

            <el-table-column label="简介" min-width="200" align="center">
                <template slot-scope="scope">
                    {{ scope.row.storyline }}
                </template>
            </el-table-column>

            <el-table-column label="演员" align="center">
                <template slot-scope="scope">
                    {{ scope.row.actors }}
                </template>
            </el-table-column>

            <el-table-column label="导演" align="center">
                <template slot-scope="scope">
                    {{ scope.row.directors }}
                </template>
            </el-table-column>

            <el-table-column label="类别" align="center">
                <template slot-scope="scope">
                    {{ scope.row.genres }}
                </template>
            </el-table-column>

            <el-table-column label="地区" align="center">
                <template slot-scope="scope">
                    {{ scope.row.regions }}
                </template>
            </el-table-column>

            <el-table-column label="评分" min-width="50" align="center">
                <template slot-scope="scope">
                    {{ scope.row.score }}
                </template>
            </el-table-column>

            <el-table-column label="上映日期" min-width="100" align="center">
                <template slot-scope="scope">
                    {{ scope.row.releaseDate }}
                </template>
            </el-table-column>

            <el-table-column label="标签" align="center">
                <template slot-scope="scope">
                    {{ scope.row.tags }}
                </template>
            </el-table-column>

            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-row>
                        <el-button size="mini" type="primary" @click="handleUpdate(scope.$index, scope.row)">编辑</el-button>
                    </el-row>
                    <el-row>
                        <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </el-row>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
            <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px"
                style="width: 400px; margin-left:50px;">

                <el-form-item label="电影名称" prop="name">
                    <el-input v-model="temp.name" placeholder="请输入" style="width:600px;" />
                </el-form-item>

                <el-form-item label="电影别名" prop="alias">
                    <el-input v-model="temp.alias" placeholder="请输入" style="width:600px;" />
                </el-form-item>

                <el-form-item label="电影评分" prop="score">
                    <el-rate v-model="temp.score" :max="10" allow-half show-score text-color="#ff9900" />
                </el-form-item>
                <el-form-item label="海报URL" prop="cover">
                    <el-input v-model="temp.cover" placeholder="请输入" style="width:600px;" />
                </el-form-item>

                <el-form-item label="演员" prop="actors">
                    <el-input v-model="temp.actors" placeholder="请输入" style="width:600px;" />
                </el-form-item>

                <el-form-item label="导演" prop="directors">
                    <el-input v-model="temp.directors" placeholder="请输入" style="width:600px;" />
                </el-form-item>

                <el-form-item label="标签" prop="tags">
                    <el-input v-model="temp.tags" placeholder="请输入" style="width:600px;" />
                </el-form-item>

                <el-form-item label="简介" prop="storyline">
                    <el-input v-model="temp.storyline" :autosize="{ minRows: 2, maxRows: 10 }" type="textarea"
                        placeholder="请输入" style="width:500px;" />
                </el-form-item>

                <el-form-item label="类别" prop="genres">
                    <el-select v-model="temp.genres_arr" multiple placeholder="请选择" style=" width:500px;">
                        <el-option v-for="item in this.genres" :key="item.id" :label="item.text" :value="item.mask">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="地区" prop="regions">
                    <el-select v-model="temp.regions_arr" multiple placeholder="请选择" style=" width:500px;">
                        <el-option v-for="item in this.regions" :key="item.id" :label="item.text" :value="item.mask">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="上映日期" prop="regions">
                    <el-date-picker v-model="temp.releaseDate" align="right" type="date" placeholder="选择日期"
                        format="yyyy年MM月dd日" value-format="yyyy年MM月dd日" />
                </el-form-item>

            </el-form>

            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">
                    取消
                </el-button>
                <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">
                    确认
                </el-button>
            </div>
        </el-dialog>

        <el-row>
            <el-col :span="10">
                <el-pagination background layout="prev, pager, next, jumper, ->" @current-change="handleCurrentPageChange"
                    :page-count="this.pages" />
            </el-col>
        </el-row>
    </div>
</template>
<script>
import { deleteMovieById, findAllMovies, findMoviesByName, insertMovie, setMovieById, updateMovieById } from '@/api/table'
export default {
    data() {
        return {
            list: [
                {
                    movieId: null,
                    name: null,
                    alias: null,
                    cover: null,
                    directors: null,
                    actors: null,
                    score: 0,
                    genres: null,
                    genresMask: 0,
                    duration: null,
                    languages: null,
                    regions: null,
                    regionsMask: 0,
                    releaseDate: null,
                    storyline: null,
                    tags: null,
                    year: null
                }],
            temp: {
                movieId: null,
                name: null,
                alias: null,
                cover: null,
                directors: null,
                actors: null,
                score: null,
                genres: null,
                genresMask: 0,
                genres_arr: [],
                duration: null,
                languages: null,
                regions: null,
                regionsMask: 0,
                regions_arr: [],
                releaseDate: null,
                storyline: null,
                tags: null,
                year: null
            },
            chargeRule: [],
            listLoading: true,
            dialogStatus: "",
            dialogFormVisible: false,
            chargeRuleTableVisible: false,
            visible: false,
            pageType: 0,
            rules: {

            },
            textMap: {
                update: '编辑',
                create: '创建'
            },
            keyword: "",
            pages: null,
            curPage: 1,
            genres:
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
            regions: [
                { id: 0, text: '中国大陆', type: 'info', mask: 1 },
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
                { id: 28, text: '芬兰', type: 'info', mask: 0x1 << 28 },
                { id: 29, text: '伊朗', type: 'info', mask: 536870912 },
                { id: 30, text: '爱尔兰', type: 'info', mask: 1073741824 },
                { id: 31, text: '其他', type: 'info', mask: 2147483648 }
            ],
        }
    },
    created() {
        this.fetchData(1)
    },
    computed: {

    },
    methods: {
        fetchData(pageNum = 1) {
            this.listLoading = true
            if (this.keyword == "" || this.keyword == null) {
                findAllMovies(pageNum).then(response => {
                    this.list = response.data.records
                    this.listLoading = false
                    this.pages = response.data.pages
                    this.curPage = response.data.current
                })
            }
            else {
                findMoviesByName(this.keyword, pageNum).then((response) => {
                    this.list = response.data.records
                    this.listLoading = false
                    this.pages = response.data.pages
                    this.curPage = response.data.current
                })
            }
        },
        resetTemp() {
            this.temp = {
                movieId: null,
                name: null,
                alias: null,
                cover: null,
                directors: null,
                actors: null,
                score: 0,
                genres: null,
                genres_arr: [],
                genresMask: 0,
                duration: null,
                languages: null,
                region: null,
                regions_arr: [],
                regionsMask: 0,
                release_date: null,
                storyline: null,
                tags: null,
                year: null
            }
        },
        handleDelete(index, row) {
            //弹窗提示
            this.$confirm('确定要删除这条数据吗', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                //调用url
                deleteMovieById(row.movieId).then(response => {
                    //成功
                    if (response.data["影响行数"] !== 0) {
                        this.$message({
                            message: '删除成功',
                            type: 'success'
                        });
                    }
                    //失败
                    else {
                        this.$message.error('删除失败');
                    }
                    this.$nextTick(this.fetchData(this.curPage))
                }).catch(() => {
                    //取消操作
                    this.$message({
                        type: 'info',
                        message: '已取消'
                    });
                });
            })
        },
        handleUpdate(index, row) {
            this.temp = Object.assign({}, this.list[index])
            // this.temp.genres_arr = this.transMasktoArr(this.temp.genresMask)
            // this.temp.regions_arr = this.transMasktoArr(this.temp.regionsMask)
            this.dialogStatus = 'update'
            this.dialogFormVisible = true
            this.$nextTick(() => {
                this.$refs['dataForm'].clearValidate()
            })
        },
        handleCreate() {
            this.resetTemp()
            this.dialogStatus = 'create'
            this.dialogFormVisible = true
        },
        createData() {
            this.$refs['dataForm'].validate((valid) => {

                this.temp.genresMask = this.transArrtoMask(this.temp.genres_arr)
                this.temp.regionsMask = this.transArrtoMask(this.temp.regions_arr)
                this.temp.genres = this.transGenresMasktoStr(this.temp.genresMask)
                this.temp.regions = this.transRegionsMasktoStr(this.temp.regionsMask)

                if (valid) {
                    insertMovie(this.temp).then((response) => {
                        if (response.data["影响行数"] !== 0) {
                            this.dialogFormVisible = false
                            this.$message({
                                message: '添加成功',
                                type: 'success'
                            });
                            this.$nextTick(this.fetchData(this.pages))
                        } else {
                            this.$message.error('添加失败');
                        }
                    })
                }
            })
        },
        updateData() {
            this.$refs['dataForm'].validate((valid) => {
                if (valid) {
                    const index = this.list.findIndex(v => v.movieId === this.temp.movieId)

                    this.temp.genresMask = this.transArrtoMask(this.temp.genres_arr)
                    this.temp.regionsMask = this.transArrtoMask(this.temp.regions_arr)
                    this.temp.genres = this.transGenresMasktoStr(this.temp.genresMask)
                    this.temp.regions = this.transRegionsMasktoStr(this.temp.regionsMask)

                    this.list.splice(index, 1, this.temp)
                    console.log(this.temp)

                    setMovieById(this.temp).then((response) => {
                        if (response.data["影响行数"] !== 0) {
                            this.dialogFormVisible = false
                            this.$message({
                                message: '更新成功',
                                type: 'success'
                            });
                        } else {
                            this.$message.error('更新失败');
                        }
                        this.$nextTick(this.fetchData(this.curPage))
                    })
                }
            })
        },
        handleCurrentPageChange(newPage) {
            this.fetchData(newPage)
        },
        transArrtoMask(arr) {
            let mask = 0;
            arr.forEach((elem) => {
                mask |= elem
            })
            return mask
        },
        transMasktoArr(mask) {
            let arr = [];
            let bit = 1;
            while (bit <= (0x1 << 31)) {
                if ((bit & mask) != 0)
                    arr.push(bit)
                bit <<= 1;
            }
            return arr
        },
        transRegionsMasktoStr(mask) {
            let str = ""
            let i = 0
            this.regions.forEach(elem => {
                if ((mask & elem.mask) != 0) {
                    if (i != 0)
                        str += '/'
                    str += elem.text
                    i++
                }
            })
            return str
        },
        transGenresMasktoStr(mask) {
            let str = ""
            let i = 0
            this.genres.forEach(elem => {
                if ((mask & elem.mask) != 0) {
                    if (i != 0)
                        str += '/'
                    str += elem.text
                    i++
                }
            })
            return str
        }
    }
}
</script>
  