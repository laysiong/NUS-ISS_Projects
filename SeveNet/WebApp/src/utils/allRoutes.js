const allRoutes = [
    { path: '/dashboard', component: 'Dashboard', menuName: 'Dashboard'},
    { path: '/employees', component: 'UserList', menuName: 'Users', children: [
            { path: '/users', menuName: 'User List' },
            { path: '/users/add', menuName: 'Add User' },
            { path: '/users/edit', menuName: 'Edit User' },
            { path: '/users/csvlist', menuName:'CSV Lists'},
        ] },
    { path: '/roles', component: 'RoleList', menuName: 'Roles', children: [
            { path: '/roles', menuName: 'Roles List' },
            { path: '/roles/add', menuName: 'Add Role' },
            { path: '/roles/edit', menuName: 'Edit Role' },
        ] },
    { path: '/auths', component: 'AuthList', menuName: 'Auths', children: [
            { path: '/auths', menuName: 'Auth List' },
            { path: '/auths/add', menuName: 'Add Auth' },
            { path: '/auths/edit', menuName: 'Edit Auth' },
        ] },
    {path: '/reports', component: 'Reports', menuName: 'Report', children: [
            { path: '/reports', menuName: 'ReportList' },
        ] },
    { path: '/notifications', component: 'NotificationList', menuName: 'Notification', children: [
            { path: '/notificationlist', menuName: 'NotificationList' },
            { path: '/sendNotification', menuName: 'Send Notification' },
        ] },
    { path: '/labels', component: 'LabelList', menuName: 'Label', children: [
            { path: '/labellist', menuName: 'Label List' },
            { path: '/labels/add', menuName: 'Add Label' },
            { path: '/labels/edit', menuName: 'Edit Label' },
        ] },
    { path: '/public', component: 'PostList', menuName: 'Public', children: [
            { path: '/mainmenu', menuName: 'New Feeds' },
            { path: '/userProfile', menuName: 'User Profile' },
            { path: '/friends', menuName: 'Friends' },
            { path: '/userdetails', menuName:'User Profile'},
            { path: '/postsdetails', menuName:"Post Details"},
            { path: '/lobby', menuName:"Lobby"},
            { path: '/search', menuName: 'Search Result' },
        ] },
]

export const getAllRoutes = allRoutes;