package com.jiawa.wiki.controller;

import com.jiawa.wiki.domain.Ebook;
import com.jiawa.wiki.req.EbookReq;
import com.jiawa.wiki.resp.CommonResp;
import com.jiawa.wiki.resp.EbookResp;
import com.jiawa.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Autowired
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp ebookList(EbookReq req) {
        // In controller layer do not use the original ebook, instead we use EbookResp
        // to control the output to frontend
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        List<EbookResp> list  = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }
}
