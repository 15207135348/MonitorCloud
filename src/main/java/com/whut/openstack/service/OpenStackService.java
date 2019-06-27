package com.whut.openstack.service;

import com.whut.openstack.domain.*;

import java.util.List;

public interface OpenStackService {

    //token
    OpenStackToken adminToken();
    OpenStackToken getToken(String username);

    //项目
    List<OpenStackTeam> listTenant(OpenStackToken token);
    List<OpenStackTeam> listTenantOfUser(OpenStackToken token, String userId);
    OpenStackTeam showTenant(OpenStackToken token, String tenantId);
    OpenStackTeam createTenant(OpenStackToken token, String tenantName, String description);
    boolean deleteTenant(OpenStackToken token, String tenantId);

    //用户
    OpenStackUser getUser(String username);
    List<OpenStackUser> listUser(OpenStackToken token);
    OpenStackUser showUser(OpenStackToken token, String userId);
    OpenStackUser createUser(OpenStackToken token, OpenStackUser user);
    boolean changePassword(OpenStackToken token, String userId, String original, String password);
    boolean deleteUser(OpenStackToken token, String userId);

    //虚拟机
    String createVHost(OpenStackToken token, String hostname, String imageRef, String flavorRef, String userData, String metadata);
    boolean deleteVHost(OpenStackToken token, String hostId);
    VirtualHost getHostInfo(OpenStackToken token, String hostId);
    List<VirtualHost> getAllHostInfo(OpenStackToken token);

    /*------------------action on VHost--------------------*/
    boolean forceDeleteVHost(OpenStackToken token, String hostId);
    boolean startVHost(OpenStackToken token, String hostId);
    boolean stopVHost(OpenStackToken token, String hostId);
    boolean rebootVHost(OpenStackToken token, String hostId, boolean graceful);
    boolean pauseVHost(OpenStackToken token, String hostId);
    boolean unpauseVHost(OpenStackToken token, String hostId);
    boolean lockVHost(OpenStackToken token, String hostId);
    boolean unlockVHost(OpenStackToken token, String hostId);
    boolean suspendVHost(OpenStackToken token, String hostId);
    boolean resumeVHost(OpenStackToken token, String hostId);
    boolean shelveVHost(OpenStackToken token, String hostId);
    boolean unshelveVHost(OpenStackToken token, String hostId);
    String showConsoleOutput(OpenStackToken token, String hostId, int line);

    String getRDPConsole(OpenStackToken token, String hostId);
    String getSerialConsole(OpenStackToken token, String hostId);
    String getVNCConsole(OpenStackToken token, String hostId);

    VirtualHostFlavor showFlavor(OpenStackToken token, String flavorId);
    List<VirtualHostFlavor> listFlavor(OpenStackToken token);
    List<VirtualHostFlavor> listFlavorDetail(OpenStackToken token);
    VirtualHostImage showImage(OpenStackToken token, String imageId);
    List<VirtualHostImage> listImage(OpenStackToken token);

}
