package com.mm.gemini.core.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mm.gemini.base.controller.BaseController;
import com.mm.gemini.base.enums.ResponseInfoEnum;
import com.mm.gemini.base.pojo.ResponseErrorVO;
import com.mm.gemini.base.pojo.ResponseListVO;
import com.mm.gemini.base.pojo.ResponseObjectVO;
import com.mm.gemini.base.pojo.ResponseSuccessVO;
import com.mm.gemini.core.model.AlbumPic;
import com.mm.gemini.core.model.AlbumSet;
import com.mm.gemini.core.pojo.dto.AlbumSetReleaseDTO;
import com.mm.gemini.core.pojo.vo.AlbumSetViewVO;
import com.mm.gemini.core.service.AlbumPicService;
import com.mm.gemini.core.service.AlbumSetService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("album/set")
public class AlbumController extends BaseController {
    @Autowired
    AlbumSetService albumSetService;
    @Autowired
    AlbumPicService albumPicService;


    /**
     * 发布相册集
     * @param setPostDTO
     * @return
     */
    @ApiOperation("发布相册集")
    @PostMapping("/release")
    public ResponseEntity release(
            AlbumSetReleaseDTO setPostDTO
    ) {
        //新增集合
        AlbumSet albumSet = new AlbumSet();
        albumSet.setDescription(setPostDTO.getDescription());
        albumSet.setLocation(setPostDTO.getLocation());
        albumSet.setUid(getUid());
        if (albumSetService.save(albumSet)) {
            //批量新增图片集合
            List<AlbumPic> albumPics = new ArrayList<>();
            for (String url : setPostDTO.getUrls()) {
                AlbumPic albumPic = new AlbumPic();
                albumPic.setUrl(url);
                albumPic.setSetId(albumSet.getId());
                albumPics.add(albumPic);
            }
            if (albumPicService.saveBatch(albumPics)) {
                return new ResponseEntity<>(new ResponseSuccessVO(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseErrorVO(ResponseInfoEnum.FAIL.msg(), 400), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(new ResponseErrorVO(ResponseInfoEnum.FAIL.msg(), 400), HttpStatus.OK);
        }
    }

    /**
     * 查看时光相册集浏览信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("查看时光相册集浏览信息")
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
            IPage<AlbumSetViewVO> iPage = albumSetService.getViewInfo(pageNum, pageSize, getConIds());
            if (iPage.getRecords().size() < 1) {
                return new ResponseEntity<>(
                        new ResponseSuccessVO(ResponseInfoEnum.NOT_FOUND.msg(), HttpStatus.OK.value()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new ResponseListVO<>(iPage.getCurrent(), iPage.getSize(), iPage.getPages(), iPage.getRecords()), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取相册浏览信息失败 " + JSON.toJSONString(e));
            return new ResponseEntity<>(
                    new ResponseErrorVO(ResponseInfoEnum.SYS_ERROR.msg(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 删除图片集
     * @param id
     * @return
     */
    @ApiOperation("删除图片集")
    @GetMapping("/delAlbumSet")
    public ResponseEntity delAlbumSet(Long id){
        UpdateWrapper<AlbumSet> updateWrapper  = new UpdateWrapper<>();
        boolean isUpdate  = albumSetService.update(
                updateWrapper.eq("id",id)
                .set(true,"is_del",1)
        );
        UpdateWrapper<AlbumPic> updateWrapper2  = new UpdateWrapper<>();
        albumPicService.update(
                updateWrapper2.eq("set_id",id)
                        .set(true,"is_del",1)
        );
        if (isUpdate){
            return new ResponseEntity<>(new ResponseSuccessVO(),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new ResponseErrorVO(),HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * 删除图片
     * @param id
     * @return
     */
    @ApiOperation("删除图片")
    @GetMapping("/delAlbumPic")
    public ResponseEntity delAlbumPic(Long id){
        UpdateWrapper<AlbumPic> updateWrapper  = new UpdateWrapper<>();
        boolean isUpdate  = albumPicService.update(
                updateWrapper.eq("id",id)
                        .set(true,"is_del",1)
        );
        if (isUpdate){
            return new ResponseEntity<>(new ResponseSuccessVO(),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new ResponseErrorVO(),HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 获取发布照片总数
     * @return
     */
    @ApiOperation("获取发布照片总数")
    @GetMapping("/getPostPicTotal")
    public ResponseEntity getPostPicTotal(){
        try {
            Long count =  albumSetService.getOurPostPicTotalCount(getConIds());
            return ResponseEntity.ok(new ResponseObjectVO<>(count));
        } catch (Exception e) {
            e.printStackTrace();
        log.error("获取发布照片总数失败 " + JSON.toJSONString(e));
            return new ResponseEntity<>(
                    new ResponseErrorVO(ResponseInfoEnum.SYS_ERROR.msg(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * 获取我们发布的次数
     * @return
     */
    @ApiOperation("获取我们发布的次数")
    @GetMapping("/getOurPostCount")
    public ResponseEntity getOurPostCount(){
        QueryWrapper<AlbumSet> queryWrapper = new QueryWrapper<>();
        try {
            Integer count = albumSetService.count(queryWrapper.in("uid",getConIds()).eq("is_del",0));
            return ResponseEntity.ok(new ResponseObjectVO<>(count));
        }catch (Exception e){
            log.error("获取我们发布的次数 " + JSON.toJSONString(e));
            return new ResponseEntity<>(
                    new ResponseErrorVO(ResponseInfoEnum.SYS_ERROR.msg(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
