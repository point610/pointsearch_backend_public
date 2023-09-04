package com.pointsearch.backend.job.once;

import com.pointsearch.backend.esdao.PostEsDao;
import com.pointsearch.backend.model.dto.post.PostEsDTO;
import com.pointsearch.backend.service.PostService;
import com.pointsearch.backend.model.entity.Post;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 全量同步帖子到 es
 */
@Component
@Slf4j
public class FullSyncPostToEs implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Resource
    private PostEsDao postEsDao;

    /**
     * 批量插入数据
     *
     * @param args
     * @return void
     * @Author point
     * @Date 17:43 2023/8/25
     **/
    @Override
    public void run(String... args) {
        // 从数据库查询全部的post数据
        List<Post> postList = postService.list();
        if (CollectionUtils.isEmpty(postList)) {
            return;
        }
        List<PostEsDTO> postEsDTOList = postList.stream().map(PostEsDTO::objToDto).collect(Collectors.toList());
        final int pageSize = 500;
        // 数据的数量
        int total = postEsDTOList.size();
        log.info("FullSyncPostToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            // 批量插入数据的最后的数量
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            // 将数据库中的数据加入到es中
            postEsDao.saveAll(postEsDTOList.subList(i, end));
        }
        log.info("FullSyncPostToEs end, total {}", total);
    }
}
