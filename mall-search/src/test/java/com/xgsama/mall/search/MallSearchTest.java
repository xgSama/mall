package com.xgsama.mall.search;

import com.alibaba.fastjson.JSON;
import com.xgsama.mall.search.config.ElasticSearchConfig;
import com.xgsama.mall.search.service.impl.MallSearchServiceImpl;
import com.xgsama.mall.search.vo.SearchParam;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * MallSearchTest
 *
 * @author : xgSama
 * @date : 2021/10/6 16:28:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MallSearchTest {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private MallSearchServiceImpl searchService;

    @Test
    public void testSearchService() {
        searchService.search(new SearchParam());
    }

    @Test
    public void contextLoads() {
        System.out.println(client);
    }

    @Test
    public void indexData() throws IOException {
        IndexRequest request = new IndexRequest("test");
        request.id("t1");
//        request.source("userName", "zhangsan", "age", 18);
        User user = new User("zhangsan", 10, "男");
        request.source(JSON.toJSONString(user), XContentType.JSON);

        IndexResponse index = client.index(request, ElasticSearchConfig.COMMON_OPTIONS);

        System.out.println(index);
    }

    @Test
    public void searchData() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bank");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("address", "mill"));

        System.out.println(sourceBuilder.toString());
        searchRequest.source(sourceBuilder);

        SearchResponse response = client.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(response);
    }

    class User {
        public String name;
        public int age;
        public String gender;

        public User() {
        }

        public User(String name, int age, String gender) {
            this.name = name;
            this.age = age;
            this.gender = gender;
        }
    }
}
