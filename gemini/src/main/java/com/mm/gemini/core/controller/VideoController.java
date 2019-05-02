package com.mm.gemini.core.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mm.gemini.base.controller.BaseController;
import com.mm.gemini.base.enums.ResponseInfoEnum;
import com.mm.gemini.base.pojo.ResponseErrorVO;
import com.mm.gemini.base.pojo.ResponseListVO;
import com.mm.gemini.base.pojo.ResponseObjectVO;
import com.mm.gemini.base.pojo.ResponseSuccessVO;
import com.mm.gemini.core.model.Video;
import com.mm.gemini.core.pojo.dto.VideoReleaseDTO;
import com.mm.gemini.core.service.VideoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("video")
@Slf4j
public class VideoController extends BaseController{
    @Autowired
    VideoService videoService;

    /**
     * 发布日记
     *
     * @param videoReleaseDTO
     * @return
     */
    @ApiOperation("发布日记")
    @PostMapping("/release")
    public ResponseEntity release(
            VideoReleaseDTO videoReleaseDTO
    ) {

        Video video = new Video();
        video.setDescription(videoReleaseDTO.getDescription());
        video.setUrl(videoReleaseDTO.getUrl());
        video.setName(videoReleaseDTO.getName());
        video.setUid(getUid());

        try {
            if (videoService.save(video)) {
                return new ResponseEntity<>(new ResponseSuccessVO(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseErrorVO(ResponseInfoEnum.FAIL.msg(), 400), HttpStatus.OK);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("发布视频 失败 " + JSON.toJSONString(e));
            return new ResponseEntity<>(
                    new ResponseErrorVO(ResponseInfoEnum.SYS_ERROR.msg(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 视频浏览信息
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("分页获取视频浏览信息")
    @GetMapping("/view")
    public ResponseEntity view(
            @ApiParam(name = "pageNum", value = "页码")
            @RequestParam(name = "pageNum", defaultValue = "1")
                    Integer pageNum,
            @ApiParam(name = "pageSize", value = "分页大小")
            @RequestParam(name = "pageSize", defaultValue = "5")
                    Integer pageSize
    ) {
        try {
            QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
            IPage<Video> iPage = videoService.page(
                    new Page<>(pageNum, pageSize),
                    queryWrapper
                            .in("uid",getConIds())
                            .eq("is_del", 0)
                            .orderByDesc("gmt_create")
            );

            if (iPage.getRecords().size() < 1) {
                return new ResponseEntity<>(
                        new ResponseSuccessVO(ResponseInfoEnum.NOT_FOUND.msg(), HttpStatus.OK.value()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new ResponseListVO<>(iPage.getCurrent(), iPage.getSize(), iPage.getPages(), iPage.getRecords()), HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("分页获取视频浏览信息失败 " + JSON.toJSONString(e));
            return new ResponseEntity<>(
                    new ResponseErrorVO(ResponseInfoEnum.SYS_ERROR.msg(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 删除视频
     *
     * @param id
     * @return
     */
    @ApiOperation("删除视频")
    @GetMapping("/del")
    public ResponseEntity del(Long id) {
        UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
        boolean isUpdate = videoService.update(
                updateWrapper.eq("id", id)
                        .eq("uid", getUid())
                        .set(true, "is_del", 1)
        );
        if (isUpdate) {
            return new ResponseEntity<>(new ResponseSuccessVO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseErrorVO(), HttpStatus.BAD_REQUEST);
        }
    }




    /**
     * 获取我们发布的视频总数
     *
     * @return
     */
    @ApiOperation("获取我们发布的视频总数")
    @GetMapping("/ourTotal")
    public ResponseEntity ourTotal() {
        try {
            QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
            Integer count = videoService.count(
                    queryWrapper.in("uid",getConIds()).eq("is_del",0)
            );
            return ResponseEntity.ok(new ResponseObjectVO<>(count));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取我们发布的视频总数失败 " + JSON.toJSONString(e));
            return new ResponseEntity<>(
                    new ResponseErrorVO(ResponseInfoEnum.SYS_ERROR.msg(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
