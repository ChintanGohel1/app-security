
-- Insert Users
INSERT INTO `users` (`email`, `enabled`, `first_name`, `last_name`, `password`) VALUES ('vinit.solanki@indianic.com', b'1', 'Vinit', 'Solanki', '$2a$10$5.xOVYRKkFtHYN78vWjLjONzPHFY6h3F6aHc8tfvRgq');

-- Insert Roles
INSERT INTO `roles` (`id`,`rolename`) VALUES (1,'ADMIN');

-- Insert Permissions
INSERT INTO `permissions` (`id`,`permissionname`) VALUES (1,'USER_LIST_GET');
INSERT INTO `permissions` (`id`,`permissionname`) VALUES (2,'USER_POST');
INSERT INTO `permissions` (`id`,`permissionname`) VALUES (3,'USER_GET');
INSERT INTO `permissions` (`id`,`permissionname`) VALUES (4,'USER_PUT');
INSERT INTO `permissions` (`id`,`permissionname`) VALUES (5,'USER_DELETE');
INSERT INTO `permissions` (`id`,`permissionname`) VALUES (6,'USER_LIST_GET_NEW');

INSERT INTO `role_permissions` (`permission_id`,`role_id`) VALUES (1,1);
INSERT INTO `role_permissions` (`permission_id`,`role_id`) VALUES (2,1);
INSERT INTO `role_permissions` (`permission_id`,`role_id`) VALUES (3,1);
INSERT INTO `role_permissions` (`permission_id`,`role_id`) VALUES (4,1);
INSERT INTO `role_permissions` (`permission_id`,`role_id`) VALUES (5,1);
INSERT INTO `role_permissions` (`permission_id`,`role_id`) VALUES (6,1);