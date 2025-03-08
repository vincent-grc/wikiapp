package com.jiawa.wiki.service;

import ch.qos.logback.core.util.StringUtil;
import com.jiawa.wiki.domain.Ebook;
import com.jiawa.wiki.domain.EbookExample;
import com.jiawa.wiki.mapper.EbookMapper;
import com.jiawa.wiki.req.EbookReq;
import com.jiawa.wiki.resp.EbookResp;
import com.jiawa.wiki.utils.CopyUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    //Both annotation are valid
    @Resource
    //@Autowired
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {
        EbookExample example = new EbookExample();
        EbookExample.Criteria criteria = example.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        List<Ebook> ebookList = ebookMapper.selectByExample(example);

        return CopyUtil.copyList(ebookList, EbookResp.class);
    }
}
