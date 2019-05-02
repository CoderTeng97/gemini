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
import com.mm.gemini.core.model.Note;
import com.mm.gemini.core.model.NoteContent;
import com.mm.gemini.core.pojo.dto.NoteModifiedDTO;
import com.mm.gemini.core.pojo.dto.NoteReleaseDTO;
import com.mm.gemini.core.service.NoteContentService;
import com.mm.gemini.core.service.NoteService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/note")
public class NoteController extends BaseController {

    @Autowired
    NoteService noteService;
    @Autowired
    NoteContentService noteContentService;

    /**
     * 发布日记
     *
     * @param noteReleaseDTO
     * @return
     */
    @ApiOperation("发布日记")
    @PostMapping("/release")
    public ResponseEntity release(
            NoteReleaseDTO noteReleaseDTO
    ) {

        Note note = new Note();
        note.setTitle(noteReleaseDTO.getTitle());
        note.setUid(getUid());
        note.setIsOpen(noteReleaseDTO.getIsOpen());
        try {
            if (noteService.save(note)) {
                NoteContent noteContent = new NoteContent();
                noteContent.setContent(noteReleaseDTO.getContent());
                noteContent.setNid(note.getId());
                if (noteContentService.save(noteContent)) {
                    return ResponseEntity.ok(new ResponseObjectVO<>(note));
                } else {
                    //内容发布失败删除日记
                    noteService.removeById(note.getId());
                    return new ResponseEntity<>(new ResponseErrorVO(ResponseInfoEnum.FAIL.msg(), 400), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(new ResponseErrorVO(ResponseInfoEnum.FAIL.msg(), 400), HttpStatus.OK);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("发布日记 失败 " + JSON.toJSONString(e));
            return new ResponseEntity<>(
                    new ResponseErrorVO(ResponseInfoEnum.SYS_ERROR.msg(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 发布日记
     *
     * @param noteModifiedDTO
     * @return
     */
    @ApiOperation("更新日志")
    @PostMapping("/modified")
    public ResponseEntity modified(
            NoteModifiedDTO noteModifiedDTO
    ) {

        Note note = new Note();
        note.setId(noteModifiedDTO.getId());
        note.setTitle(noteModifiedDTO.getTitle());
        note.setUid(getUid());
        note.setIsOpen(noteModifiedDTO.getIsOpen());
        try {
            if (noteService.updateById(note)) {
                return ResponseEntity.ok(new ResponseObjectVO<>(note));
            } else {
                return new ResponseEntity<>(new ResponseErrorVO(ResponseInfoEnum.FAIL.msg(), 400), HttpStatus.OK);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("更新日记 失败 " + JSON.toJSONString(e));
            return new ResponseEntity<>(
                    new ResponseErrorVO(ResponseInfoEnum.SYS_ERROR.msg(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 日记浏览信息
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("获取日记浏览信息")
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
            QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
            IPage<Note> iPage = noteService.page(
                    new Page<>(pageNum, pageSize),
                    queryWrapper.eq("uid", getUid()).eq("is_del", 0).orderByDesc("gmt_create")
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
            log.error("获取日记浏览信息失败 " + JSON.toJSONString(e));
            return new ResponseEntity<>(
                    new ResponseErrorVO(ResponseInfoEnum.SYS_ERROR.msg(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation("获取日记内容")
    @GetMapping("/content")
    public ResponseEntity content(Long id) {
        QueryWrapper<NoteContent> queryWrapper = new QueryWrapper<>();
        NoteContent noteContent = noteContentService.getOne(queryWrapper.eq("nid",id));
        return ResponseEntity.ok(new ResponseObjectVO<>(noteContent)) ;
    }

    /**
     * 删除日记
     *
     * @param id
     * @return
     */
    @ApiOperation("删除日记")
    @GetMapping("/delAlbumSet")
    public ResponseEntity delAlbumSet(Long id) {
        UpdateWrapper<Note> updateWrapper = new UpdateWrapper<>();
        boolean isUpdate = noteService.update(
                updateWrapper.eq("id", id)
                        .set(true, "is_del", 1)
        );
        UpdateWrapper<NoteContent> updateWrapper2 = new UpdateWrapper<>();
        noteContentService.update(
                updateWrapper2.eq("nid", id)
                        .set(true, "is_del", 1)
        );
        if (isUpdate) {
            return new ResponseEntity<>(new ResponseSuccessVO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseErrorVO(), HttpStatus.BAD_REQUEST);
        }
    }




    /**
     * 获取我们发布的日志总数
     *
     * @return
     */
    @ApiOperation("获取我们发布的日志总数")
    @GetMapping("/getOurTotal")
    public ResponseEntity getOurTotal() {
        try {
            QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
            Integer count = noteService.count(
                    queryWrapper.in("uid",getConIds()).eq("is_del",0)
            );
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取我们发布的日志总数 " + JSON.toJSONString(e));
            return new ResponseEntity<>(
                    new ResponseErrorVO(ResponseInfoEnum.SYS_ERROR.msg(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
