package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.MemberConfirmInfoVO;
import com.atguigu.crowd.entity.MemberLauchInfoVO;
import com.atguigu.crowd.entity.ProjectVO;
import com.atguigu.crowd.entity.ReturnVO;
import com.atguigu.crowd.entity.po.*;
import com.atguigu.crowd.mapper.*;
import com.atguigu.crowd.service.api.ProjectService;
import com.atguigu.crowd.util.CrowdUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly=true)
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectPOMapper projectPOMapper;
    @Autowired
    private ProjectItemPicPOMapper projectItemPicPOMapper;
    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;
    @Autowired
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;
    @Autowired
    private TypePOMapper typePOMapper;
    @Autowired
    private TagPOMapper tagPOMapper;
    @Autowired
    private ReturnPOMapper returnPOMapper;


    @Transactional(readOnly=false, propagation= Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
    public void saveProject(ProjectVO projectVO,String memberId) {
        //1.
        ProjectPO projectPO = new ProjectPO();
        BeanUtils.copyProperties(projectVO,projectPO);

        //2.保存projectPO以获取自增Id
        projectPOMapper.insert(projectPO);
        Integer projectId = projectPO.getId();

        //3.保存typeList
        List<Integer> typeIdList = projectVO.getTypeIdList();
        typePOMapper.insertRelationshipBatch(projectId,typeIdList);

        //4.保存tagList
        List<Integer> tagIdList = projectVO.getTagIdList();
        tagPOMapper.insertRelationshipBatch(projectId,tagIdList);

        // 5.保存detailPicturePathList
        // ①从VO对象中获取detailPicturePathList
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        if(CrowdUtils.collectionEffectiveCheck(detailPicturePathList)) {

            // ②创建一个空List集合，用来存储ProjectItemPicPO对象
            List<ProjectItemPicPO> projectItemPicPOList = new ArrayList<ProjectItemPicPO>();

            // ③遍历detailPicturePathList
            for (String detailPath : detailPicturePathList) {

                // ④创建projectItemPicPO对象
                ProjectItemPicPO projectItemPicPO = new ProjectItemPicPO(null, projectId, detailPath);

                projectItemPicPOList.add(projectItemPicPO);
            }

            // ⑤根据projectItemPicPOList执行批量保存
            projectItemPicPOMapper.insertBatch(projectItemPicPOList);
        }

        //6.保存MemberLaunchInfoPO
        MemberLauchInfoVO memberLauchInfoVO = new MemberLauchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();

        BeanUtils.copyProperties(memberLauchInfoVO,memberLaunchInfoPO);

        memberLaunchInfoPO.setMemberid(Integer.parseInt(memberId));

        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

        //7.根据ReturnVO的List保存ReturnPO
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();

        List<ReturnPO> returnPOList = new ArrayList<ReturnPO>();

        for(ReturnVO returnVO : returnVOList){
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(returnVO,returnPO);
            returnPO.setProjectid(projectId);
            returnPOList.add(returnPO);
        }

        returnPOMapper.insertBatch(returnPOList);
        //8.保存memberConfirmInfoPO
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO(null,Integer.parseInt(memberId),memberConfirmInfoVO.getPaynum(),memberConfirmInfoVO.getCardnum());

        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);
    }
}
