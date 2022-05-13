SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

CREATE TABLE `requirement` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `description` varchar(1000) COLLATE utf8_bin NOT NULL,
  `status_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

INSERT INTO `role` (`id`, `name`) VALUES
(1, 'requirements_engineer'),
(2, 'testmanager'),
(3, 'testcreater'),
(4, 'tester');

CREATE TABLE `status` (
  `id` int(11) NOT NULL,
  `type` varchar(35) COLLATE utf8_bin NOT NULL,
  `name` varchar(35) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

INSERT INTO `status` (`id`, `type`, `name`) VALUES
(1, 'req', 'New'),
(2, 'req', 'Ready for Testing'),
(3, 'req', 'In Test'),
(4, 'req', 'Accepted'),
(5, 'run', 'New'),
(6, 'run', 'In Progress'),
(7, 'run', 'Fail'),
(8, 'run', 'Pass'),
(9, 'run', 'Blocked'),
(10, 'case', 'Failed'),
(11, 'case', 'Passed'),
(12, 'case', 'Not Run');

CREATE TABLE `testcase` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `description` varchar(255) COLLATE utf8_bin NOT NULL,
  `requirement_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `testrun` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `status_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `requirement_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `testrun_testcase` (
  `id` int(11) NOT NULL,
  `testrun_id` int(11) NOT NULL,
  `testcase_id` int(11) NOT NULL,
  `status_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `firstname` varchar(35) COLLATE utf8_bin NOT NULL,
  `lastname` varchar(35) COLLATE utf8_bin NOT NULL,
  `email` varchar(255) COLLATE utf8_bin NOT NULL,
  `password` varchar(255) COLLATE utf8_bin NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

INSERT INTO `user` (`id`, `firstname`, `lastname`, `email`, `password`, `role_id`) VALUES
(1, 'requirements', 'engineer', 'requirements_engineer@iu-study.org', 'password', 1),
(2, 'test', 'manager', 'testmanager@iu-study.org', 'password', 2),
(3, 'test', 'creator', 'testcreator@iu-study.org', 'password', 3),
(4, 'test', 'user', 'testcreator@iu-study.org', 'password', 4);


ALTER TABLE `requirement`
  ADD PRIMARY KEY (`id`),
  ADD KEY `status_id` (`status_id`);

ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `status`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `testcase`
  ADD PRIMARY KEY (`id`),
  ADD KEY `requirement_id` (`requirement_id`);

ALTER TABLE `testrun`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `requirement_id` (`requirement_id`),
  ADD KEY `status_id` (`status_id`) USING BTREE;

ALTER TABLE `testrun_testcase`
  ADD PRIMARY KEY (`id`),
  ADD KEY `testrun_id` (`testrun_id`),
  ADD KEY `testcase_id` (`testcase_id`),
  ADD KEY `status_id` (`status_id`);

ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD KEY `role_id` (`role_id`);


ALTER TABLE `requirement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

ALTER TABLE `role`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

ALTER TABLE `status`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

ALTER TABLE `testcase`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

ALTER TABLE `testrun`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;

ALTER TABLE `testrun_testcase`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=166;

ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;


ALTER TABLE `requirement`
  ADD CONSTRAINT `requirement_ibfk_1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`);

ALTER TABLE `testcase`
  ADD CONSTRAINT `testcase_ibfk_1` FOREIGN KEY (`requirement_id`) REFERENCES `requirement` (`id`);

ALTER TABLE `testrun`
  ADD CONSTRAINT `testrun_ibfk_1` FOREIGN KEY (`requirement_id`) REFERENCES `requirement` (`id`),
  ADD CONSTRAINT `testrun_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `testrun_ibfk_3` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`);

ALTER TABLE `testrun_testcase`
  ADD CONSTRAINT `testrun_testcase_ibfk_1` FOREIGN KEY (`testcase_id`) REFERENCES `testcase` (`id`),
  ADD CONSTRAINT `testrun_testcase_ibfk_2` FOREIGN KEY (`testrun_id`) REFERENCES `testrun` (`id`),
  ADD CONSTRAINT `testrun_testcase_ibfk_3` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`);

ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);
COMMIT;

