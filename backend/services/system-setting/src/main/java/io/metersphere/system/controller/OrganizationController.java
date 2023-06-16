package io.metersphere.system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.metersphere.sdk.constants.PermissionConstants;
import io.metersphere.sdk.dto.ProjectDTO;
import io.metersphere.sdk.util.PageUtils;
import io.metersphere.sdk.util.Pager;
import io.metersphere.sdk.util.SessionUtils;
import io.metersphere.system.dto.OrganizationDTO;
import io.metersphere.system.dto.UserExtend;
import io.metersphere.system.request.OrganizationMemberRequest;
import io.metersphere.system.request.OrganizationRequest;
import io.metersphere.system.request.ProjectRequest;
import io.metersphere.system.service.OrganizationService;
import io.metersphere.system.service.SystemProjectService;
import jakarta.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author song-cc-rock
 */
@RestController
@RequestMapping("/organization")
public class OrganizationController {

    @Resource
    private SystemProjectService systemProjectService;
    @Resource
    private OrganizationService organizationService;

    @PostMapping("/list")
    @RequiresPermissions(PermissionConstants.SYSTEM_ORGANIZATION_READ)
    public Pager<List<OrganizationDTO>> list(@Validated @RequestBody OrganizationRequest organizationRequest) {
        Page<Object> page = PageHelper.startPage(organizationRequest.getCurrent(), organizationRequest.getPageSize());
        return PageUtils.setPageInfo(page, organizationService.list(organizationRequest));
    }

    @PostMapping("/list-all")
    @RequiresPermissions(PermissionConstants.SYSTEM_ORGANIZATION_READ)
    public List<OrganizationDTO> listAll(@Validated @RequestBody OrganizationRequest organizationRequest) {
        return organizationService.list(organizationRequest);
    }

    @PostMapping("/list-member")
    @RequiresPermissions(PermissionConstants.SYSTEM_USER_READ)
    public Pager<List<UserExtend>> listMember(@Validated @RequestBody OrganizationRequest organizationRequest) {
        Page<Object> page = PageHelper.startPage(organizationRequest.getCurrent(), organizationRequest.getPageSize());
        return PageUtils.setPageInfo(page, organizationService.listMember(organizationRequest));
    }

    @PostMapping("/add-member")
    @RequiresPermissions(PermissionConstants.SYSTEM_USER_READ)
    public void addMember(@Validated @RequestBody OrganizationMemberRequest organizationMemberRequest) {
        organizationMemberRequest.setCreateUserId(SessionUtils.getUserId());
        organizationService.addMember(organizationMemberRequest);
    }

    @GetMapping("/remove-member/{organizationId}/{userId}")
    @RequiresPermissions(PermissionConstants.SYSTEM_USER_READ)
    public void removeMember(@PathVariable String organizationId, @PathVariable String userId) {
        organizationService.removeMember(organizationId, userId);
    }

    @GetMapping("/default")
    @RequiresPermissions(PermissionConstants.SYSTEM_ORGANIZATION_READ)
    public OrganizationDTO getDefault() {
        return organizationService.getDefault();
    }

    @PostMapping("/list-project")
    @RequiresPermissions(PermissionConstants.SYSTEM_PROJECT_READ)
    public Pager<List<ProjectDTO>> listProject(@Validated @RequestBody ProjectRequest projectRequest) {
        Page<Object> page = PageHelper.startPage(projectRequest.getCurrent(), projectRequest.getPageSize());
        return PageUtils.setPageInfo(page, systemProjectService.getProjectList(projectRequest));
    }
}