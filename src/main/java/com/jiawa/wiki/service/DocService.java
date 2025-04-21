package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.Content;
import com.jiawa.wiki.domain.Doc;
import com.jiawa.wiki.domain.DocExample;
import com.jiawa.wiki.mapper.ContentMapper;
import com.jiawa.wiki.mapper.DocMapper;
import com.jiawa.wiki.req.DocQueryReq;
import com.jiawa.wiki.req.DocSaveReq;
import com.jiawa.wiki.resp.DocQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.utils.CopyUtil;
import com.jiawa.wiki.utils.SnowFlake;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    //Both annotation are valid
    @Resource
    //@Autowired
    private DocMapper docMapper;

    //@Autowired
    //private SnowFlake snowFlake;

    @Autowired
    private ContentMapper contentMapper;

    public List<DocQueryResp> all(Long ebookId) {
        DocExample example = new DocExample();
        example.createCriteria().andEbookIdEqualTo(ebookId);
        example.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(example);

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        return list ;
    }

    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample example = new DocExample();
        example.setOrderByClause("sort asc");
        DocExample.Criteria criteria = example.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(example);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("Total rows: {}", pageInfo.getTotal());
        LOG.info("Total pages: {}", pageInfo.getPages());

        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(CopyUtil.copyList(docList, DocQueryResp.class));

        return pageResp ;
    }

    /**
     * Save the changes made from frontend
     */
    public void save(@Valid DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // Add a new record
            docMapper.insert(doc);

            content.setId(doc.getId()); // IMPORTANT: set the ID
            contentMapper.insert(content);
        } else {
            docMapper.updateByPrimaryKey(doc);
            content.setId(doc.getId());
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(CopyUtil.copy(req, Content.class));
            if (count == 0) {
                contentMapper.insert(CopyUtil.copy(req, Content.class));
            }
        }
    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        DocExample example = new DocExample();
        DocExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(example);
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        }
        return content.getContent();
    }
}
