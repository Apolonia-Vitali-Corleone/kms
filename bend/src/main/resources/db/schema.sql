DROP TABLE IF EXISTS tag;
CREATE TABLE tag (
  id BIGINT PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  remark VARCHAR(255) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS visibility;
CREATE TABLE visibility (
  id BIGINT PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  remark VARCHAR(255) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS knowledge;
CREATE TABLE knowledge (
  id BIGINT PRIMARY KEY,
  category_name VARCHAR(255) NOT NULL,
  title VARCHAR(255) NOT NULL,
  tag_name VARCHAR(255) NOT NULL,
  visibility_name VARCHAR(128) NOT NULL,
  keywords VARCHAR(255) NULL,
  status TINYINT NOT NULL DEFAULT 1,
  summary VARCHAR(500) NULL,
  question_no INT NOT NULL DEFAULT 1,
  content MEDIUMTEXT NOT NULL,
  attachments MEDIUMTEXT NULL,
  created_by VARCHAR(64) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- seed data for tag
INSERT INTO tag (id, name, remark) VALUES
  (1, 'Tag1', NULL),
  (2, 'Tag2', NULL),
  (3, 'Tag3', NULL),
  (4, 'Tag4', NULL),
  (5, 'Tag5', NULL),
  (6, 'Tag6', NULL),
  (7, 'Tag7', NULL),
  (8, 'Tag8', NULL),
  (9, 'Tag9', NULL),
  (10, 'Tag10', NULL),
  (11, 'Tag11', NULL),
  (12, 'Tag12', NULL),
  (13, 'Tag13', NULL),
  (14, 'Tag14', NULL),
  (15, 'Tag15', NULL),
  (16, 'Tag16', NULL),
  (17, 'Tag17', NULL),
  (18, 'Tag18', NULL),
  (19, 'Tag19', NULL),
  (20, 'Tag20', NULL);

-- seed data for visibility
INSERT INTO visibility (id, name, remark) VALUES
  (1, 'Public', NULL),
  (2, 'Private', NULL),
  (3, 'Team', NULL),
  (4, 'Dept', NULL),
  (5, 'Internal', NULL),
  (6, 'External', NULL),
  (7, 'Partner', NULL),
  (8, 'VIP', NULL),
  (9, 'Guest', NULL),
  (10, 'Restricted', NULL),
  (11, 'Level1', NULL),
  (12, 'Level2', NULL),
  (13, 'Level3', NULL),
  (14, 'Level4', NULL),
  (15, 'Level5', NULL),
  (16, 'Level6', NULL),
  (17, 'Level7', NULL),
  (18, 'Level8', NULL),
  (19, 'Level9', NULL),
  (20, 'Level10', NULL);

-- seed data for knowledge
INSERT INTO knowledge (id, category_name, title, tag_name, visibility_name, keywords, status, summary, question_no, content, attachments, created_by) VALUES
  (1001, 'CategoryA', 'Knowledge 1', 'Tag1', 'Public', 'k1,k2', 1, 'Summary 1', 1, 'Content 1', '[]', 'admin'),
  (1002, 'CategoryA', 'Knowledge 2', 'Tag2', 'Public', 'k1,k2', 1, 'Summary 2', 2, 'Content 2', '[]', 'admin'),
  (1003, 'CategoryA', 'Knowledge 3', 'Tag3', 'Public', 'k1,k2', 1, 'Summary 3', 3, 'Content 3', '[]', 'admin'),
  (1004, 'CategoryA', 'Knowledge 4', 'Tag4', 'Public', 'k1,k2', 1, 'Summary 4', 4, 'Content 4', '[]', 'admin'),
  (1005, 'CategoryA', 'Knowledge 5', 'Tag5', 'Public', 'k1,k2', 1, 'Summary 5', 5, 'Content 5', '[]', 'admin'),
  (1006, 'CategoryB', 'Knowledge 6', 'Tag6', 'Private', 'k1,k2', 1, 'Summary 6', 6, 'Content 6', '[]', 'admin'),
  (1007, 'CategoryB', 'Knowledge 7', 'Tag7', 'Private', 'k1,k2', 1, 'Summary 7', 7, 'Content 7', '[]', 'admin'),
  (1008, 'CategoryB', 'Knowledge 8', 'Tag8', 'Private', 'k1,k2', 1, 'Summary 8', 8, 'Content 8', '[]', 'admin'),
  (1009, 'CategoryB', 'Knowledge 9', 'Tag9', 'Private', 'k1,k2', 1, 'Summary 9', 9, 'Content 9', '[]', 'admin'),
  (1010, 'CategoryB', 'Knowledge 10', 'Tag10', 'Private', 'k1,k2', 1, 'Summary 10', 10, 'Content 10', '[]', 'admin'),
  (1011, 'CategoryC', 'Knowledge 11', 'Tag11', 'Team', 'k1,k2', 1, 'Summary 11', 11, 'Content 11', '[]', 'admin'),
  (1012, 'CategoryC', 'Knowledge 12', 'Tag12', 'Team', 'k1,k2', 1, 'Summary 12', 12, 'Content 12', '[]', 'admin'),
  (1013, 'CategoryC', 'Knowledge 13', 'Tag13', 'Team', 'k1,k2', 1, 'Summary 13', 13, 'Content 13', '[]', 'admin'),
  (1014, 'CategoryC', 'Knowledge 14', 'Tag14', 'Team', 'k1,k2', 1, 'Summary 14', 14, 'Content 14', '[]', 'admin'),
  (1015, 'CategoryC', 'Knowledge 15', 'Tag15', 'Team', 'k1,k2', 1, 'Summary 15', 15, 'Content 15', '[]', 'admin'),
  (1016, 'CategoryD', 'Knowledge 16', 'Tag16', 'Dept', 'k1,k2', 1, 'Summary 16', 16, 'Content 16', '[]', 'admin'),
  (1017, 'CategoryD', 'Knowledge 17', 'Tag17', 'Dept', 'k1,k2', 1, 'Summary 17', 17, 'Content 17', '[]', 'admin'),
  (1018, 'CategoryD', 'Knowledge 18', 'Tag18', 'Dept', 'k1,k2', 1, 'Summary 18', 18, 'Content 18', '[]', 'admin'),
  (1019, 'CategoryD', 'Knowledge 19', 'Tag19', 'Dept', 'k1,k2', 1, 'Summary 19', 19, 'Content 19', '[]', 'admin'),
  (1020, 'CategoryD', 'Knowledge 20', 'Tag20', 'Dept', 'k1,k2', 1, 'Summary 20', 20, 'Content 20', '[]', 'admin');
