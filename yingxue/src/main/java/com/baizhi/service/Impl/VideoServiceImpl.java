package com.baizhi.service.Impl;

import com.baizhi.dao.VideoMapper;
import com.baizhi.elasticsearch.VideoRepository;
import com.baizhi.entity.Video;
import com.baizhi.service.VideoService;
import com.baizhi.utils.UUIDUtil;
import com.baizhi.utils.UploadAliyun;
import com.baizhi.vo.VideoCateVO;
import com.baizhi.vo.VideoVO;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.apache.ibatis.session.RowBounds;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.net.URL;
import java.util.*;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {
    @Resource
    private VideoMapper videoMapper;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public HashMap<String,Object> insert(Video video) {
        HashMap<String, Object> map = new HashMap<>();
        video.setId(UUIDUtil.getUUID());
        video.setUploadTime(new Date());
        videoMapper.insert(video);
        map.put("id",video.getId());
        //创建点赞 播放
        HashOperations hash = redisTemplate.opsForHash();
        hash.put("likes",video.getId(),0);
        hash.put("counts",video.getId(),0);
        return map;
    }

    @Override
    public HashMap<String, Object> queryPage(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<>();
        Video video = new Video();
        //设置当前页
        map.put("page",page);
        //查询总条数
        int count = videoMapper.selectCount(video);
        //设置总条数
        map.put("records",count);
        //计算总页数 总页数 =总条数/每页展示条数
        Integer total=count%rows==0?count/rows:count/rows+1;
        //设置总页数 =总条数/每页展示条数
        map.put("total",total);
        //返回的数据
        List<Video> videos = videoMapper.selectByRowBounds(video, new RowBounds((page - 1) * rows, rows));
        map.put("rows",videos);
        return map;
    }

    @Override
    public HashMap<String, Object> uploadFile(MultipartFile videoPath, String id) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            //1.获取文件名
            String filename = videoPath.getOriginalFilename();
            //2.修改文件名+时间戳
            String newName=new Date().getTime()+"-"+filename;
            String objectName="video/"+newName;
            //3.文件上传
            /**2.将文件上传至阿里云
             *将文件以转为字节数组上传至阿里云
             *  参数:
             *  bucketName(String):指定存储空间名
             *  fileName(String):文件名
             *  headImg(MultipartFile):MultipartFile类型的文件
             * */
            UploadAliyun.upload("yingx-mao",objectName,videoPath);
            /**3.根据阿里云视频截取封面
             *视频截取帧
             *  参数:
             *  bucketName(String):指定存储空间名
             *  fileName(String):文件名
             * */
            URL url = UploadAliyun.interceptCover("yingx-mao", objectName);
            //截取视频名拼接图片名  0:1622446770298-动画    1:mp4
            String[] split = newName.split("\\.");
            //获取名字   1622446770298-动画
            String names=split[0];
            //拼接封面名  1622446770298-动画.jpg
            String coverName=names+".jpg";
            /**4.将网络图片上传至阿里云
             *将网络文件上传至阿里云 网络地址
             *  参数:
             *  bucketName(String):指定存储空间名
             *  fileName(String):文件名
             *  netPath(String):网络地址  https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png
             * */
            UploadAliyun.internetFileUploads("yingx-mao","cover/"+coverName,url.toString());

            //4.修改用户地址 https://yingx-test.oss-cn-beijing.aliyuncs.com/
            Video video = new Video();
            video.setId(id);
            video.setVideoPath("https://yingx-mao.oss-cn-beijing.aliyuncs.com/video/"+newName);
            video.setCiverPath("https://yingx-mao.oss-cn-beijing.aliyuncs.com/"+"cover/"+coverName);
            videoMapper.updateByPrimaryKeySelective(video);
            map.put("massge","文件上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("massge","文件上传失败");
        }
        return map;
    }

    @Override
    public HashMap<String, Object> delete(String id) {

        HashMap<String, Object> map = new HashMap<>();

        try {
            Video video = videoMapper.selectByPrimaryKey(id);
            /*//获取绝对路径
            String realPath = session.getServletContext().getRealPath(user.getHeadImg());
            File file = new File(realPath);
            //判断文件 是否存在
            if (file.exists()&&file.isFile())file.delete();*/
            UploadAliyun.deleteFile("yingx-mao",video.getVideoPath());
            UploadAliyun.deleteFile("yingx-mao",video.getCiverPath());
            redisTemplate.opsForHash().delete("likes",id);
            redisTemplate.opsForHash().delete("counts",id);
            videoMapper.deleteByPrimaryKey(id);
            map.put("massge","删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("massge","删除失败");
        }
        return map;
    }

    @Override
    public HashMap<String, Object> update(Video video) {
        HashMap<String, Object> map = new HashMap<>();
        //获取旧文件名
        Video video1 = videoMapper.selectByPrimaryKey(video);
        //判断文件是否修改
        if(!"".equals(video.getVideoPath())){
            //删除旧文件
            UploadAliyun.deleteFile("yingx-mao",video1.getVideoPath());
            UploadAliyun.deleteFile("yingx-mao",video1.getCiverPath());
            map.put("statu",true);
            map.put("id",video.getId());
        }else {
            video.setVideoPath(video1.getVideoPath());
            video.setCiverPath(video1.getCiverPath());
            map.put("statu",false);
        }
        videoMapper.updateByPrimaryKeySelective(video);
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public HashMap<String,Object> queryByReleaseTime() {

        HashMap<String, Object> map = new HashMap<>();
        try {
            List<VideoVO> videoVOS = videoMapper.queryByReleaseTime();
            map.put("data",videoVOS);
            map.put("message","查询成功");
            map.put("status",100);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("data",null);
            map.put("message","查询失败");
            map.put("status",104);
        }

        return map;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public HashMap<String, Object> queryCateVideoList(String cateId) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            List<VideoCateVO> videoVOS = videoMapper.queryCateVideoList(cateId);
            System.out.println("videoVOS = " + videoVOS);
            map.put("data",videoVOS);
            map.put("message","查询成功");
            map.put("status",100);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("data",null);
            map.put("message","查询失败");
            map.put("status",104);
        }
        return map;
    }
 //   @Resource
  //  private VideoRepository videoRepository;
    @Override
    public void addEs() {
        Video video = new Video();
        List<Video> videos = videoMapper.select(video);
        for (Video video1 : videos) {
            System.out.println("video1 = " + video1);
        }
       // videoRepository.saveAll(videos);
    }

    @Override
    public void delEs() {

    }
  //  @Resource
  //  private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public List<Video> queryEs(String content) {
       /* //创建高亮字段
        HighlightBuilder.Field field = new HighlightBuilder.Field("*")
                .preTags("<span style='color:red;'>")  //前缀
                .postTags("</span>")  //后缀
                .requireFieldMatch(false); //开启多行高亮

            //创建sortBuilder   排序
       // FieldSortBuilder order = SortBuilders.fieldSort(sortField).order(SortOrder.ASC);

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices("yingx")   //指定索引
                .withTypes("video")   //指定类型
                .withQuery(QueryBuilders.queryStringQuery(content).analyzer("ik_max_word").field("title").field("description"))   //指定查询条件
               // .withFields(str)   //指定返回条件
               // .withPageable(PageRequest.of(page,size)) //指定分页
                //.withSort(order)   //指定排序条件
                .withHighlightFields(field)  //处理高亮
                .build();

        //查询返回一个集合  参数：查询条件,返回集合的泛型
        AggregatedPage<Video> videos = elasticsearchTemplate.queryForPage(nativeSearchQuery, Video.class, new SearchResultMapper() {
            //内部类   处理高亮结果
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

                ArrayList<Video> videos = new ArrayList<>();

                //拿到原始文档的数组
                SearchHit[] hits = searchResponse.getHits().getHits();
                for (SearchHit hit : hits) {

                    Video video = new Video();
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            *//*product.setId(sourceAsMap.get("id").toString());
                    product.setName(sourceAsMap.get("name").toString());
                    product.setPrice(Double.valueOf(sourceAsMap.get("price").toString()));
                    product.setCreateDate(new Date(Long.valueOf(sourceAsMap.get("createDate").toString())));
                    product.setContent(sourceAsMap.get("content").toString());*//*

                    String id = sourceAsMap.containsKey("id")?sourceAsMap.get("id").toString():"";
                    String title = sourceAsMap.containsKey("title")?sourceAsMap.get("title").toString():"";
                    String description = sourceAsMap.containsKey("description")?sourceAsMap.get("description").toString():"";
                    Date CreateDate = sourceAsMap.containsKey("uploadTime")?new Date(Long.valueOf(sourceAsMap.get("uploadTime").toString())):null;
                    String videoPath = sourceAsMap.containsKey("videoPath")?sourceAsMap.get("videoPath").toString():"";
                    String civerPath = sourceAsMap.containsKey("civerPath")?sourceAsMap.get("civerPath").toString():"";


                    //获取当前文档高亮字段map集合
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();

                        if(highlightFields.get("title")!=null){
                            String names = highlightFields.get("title").getFragments()[0].toString();
                            video.setTitle(title);
                        }


                        if(highlightFields.get("description")!=null){
                            String contents = highlightFields.get("description").getFragments()[0].toString();
                            video.setDescription(description);
                        }

                    //放入集合
                    videos.add(video);

                }
                //强转 返回
                return new AggregatedPageImpl<T>((List<T>) videos);
            }



        });*/
        //return videos.getContent();
        return null;
    }

}
