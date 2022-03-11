
CREATE TABLE MusicHub.dbo.app_role (
	id bigint IDENTITY(1,1) NOT NULL,
	name varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	CONSTRAINT PK__app_role__3213E83F2F2D65E2 PRIMARY KEY (id)
);

CREATE TABLE MusicHub.dbo.authority (
	id bigint IDENTITY(1,1) NOT NULL,
	privilege varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	CONSTRAINT PK__authorit__3213E83FA1385A9F PRIMARY KEY (id)
);

CREATE TABLE MusicHub.dbo.user_info (
	id bigint IDENTITY(1,1) NOT NULL,
	avatar_img varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	first_name varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	last_name varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	story varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	date_new datetime NULL,
	date_edit datetime NULL,
	user_new varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	user_edit varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	CONSTRAINT PK__user_inf__3213E83F3B74399C PRIMARY KEY (id)
);

CREATE TABLE MusicHub.dbo.app_user (
	id bigint IDENTITY(1,1) NOT NULL,
	username varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	email varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	password varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	enabled bit NULL,
	account_non_locked bit NULL,
	userinfo_id bigint NULL,
	date_new datetime NULL,
	date_edit datetime NULL,
	user_new varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	user_edit varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	CONSTRAINT PK__app_user__3213E83F79E60F41 PRIMARY KEY (id),
	CONSTRAINT FKkduse3aij4axmpr2o0830yttj FOREIGN KEY (userinfo_id) REFERENCES MusicHub.dbo.user_info(id)
);

CREATE INDEX index_app_username
ON app_user (username);

CREATE TABLE MusicHub.dbo.app_user_authority (
	user_id bigint NOT NULL,
	authority_id bigint NOT NULL,
	CONSTRAINT PK__app_user__1C9633D5B194680A PRIMARY KEY (user_id,authority_id),
	CONSTRAINT FK8n0l4u153hfikbao1aa2n0jh1 FOREIGN KEY (authority_id) REFERENCES MusicHub.dbo.authority(id),
	CONSTRAINT FKfocpjrj1tmhlu9vcfo47nqanp FOREIGN KEY (user_id) REFERENCES MusicHub.dbo.app_user(id)
);

CREATE TABLE MusicHub.dbo.app_user_role (
	user_id bigint NOT NULL,
	role_id bigint NOT NULL,
	CONSTRAINT PK__app_user__6EDEA1537E812C8D PRIMARY KEY (user_id,role_id),
	CONSTRAINT FK6hkq1uibwvjsusnnxrsm1gh83 FOREIGN KEY (role_id) REFERENCES MusicHub.dbo.app_role(id),
	CONSTRAINT FKfnlxi1bmv5ao8u3nf30ymq7xa FOREIGN KEY (user_id) REFERENCES MusicHub.dbo.app_user(id)
);

CREATE TABLE MusicHub.dbo.verication_token (
	id bigint IDENTITY(1,1) NOT NULL,
	user_id bigint NOT NULL,
	token uniqueidentifier NULL,
	date_new datetime NULL,
	is_send bit NULL,
	last_send datetime NULL,
	is_verify bit NULL,
	verify_date datetime NULL,
	CONSTRAINT PK__vericati__3213E83F065DED7B PRIMARY KEY (id),
	CONSTRAINT FKedoen3vaj6fm86p8sr27sk3is FOREIGN KEY (user_id) REFERENCES MusicHub.dbo.app_user(id)
);

CREATE TABLE MusicHub.dbo.genre (
	id bigint IDENTITY(1000,1) primary key,
	name nvarchar(70) NOT NULL,
	description text NULL,
)

CREATE TABLE artist(
	id bigint IDENTITY(1000,1) primary key,
	nick_name nvarchar(100) not null,
	birthday date null,
	gender bit default 1 not null,
	avatar_img_url text null,
	cover_img_url text null,
	is_composer bit,
	is_singer bit,
	is_active bit default 1 not null
)

CREATE TABLE MusicHub.dbo.album (
	id bigint IDENTITY(1000,1) primary key,
	name nvarchar(70) NOT NULL,
	description text NULL,
	release_date datetime NOT NULL,
	image_url nvarchar(255),
	user_id bigint not null,
	date_new datetime NULL,
	date_edit datetime NULL,
	user_new varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	user_edit varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	CONSTRAINT FK_album_user_id FOREIGN KEY (user_id) REFERENCES MusicHub.dbo.app_user(id),
)

CREATE TABLE MusicHub.dbo.artist_album (
	user_id bigint not null,
	album_id bigint not null,
	CONSTRAINT PK_artist_id__album_id PRIMARY KEY (user_id, album_id),
	CONSTRAINT FK_artist_album__user_id FOREIGN KEY (user_id) REFERENCES MusicHub.dbo.app_user(id),
	CONSTRAINT FK_artist_album__album_id FOREIGN KEY (album_id) REFERENCES MusicHub.dbo.album(id)
)

CREATE TABLE category(
	id bigint IDENTITY(1000,1) primary key,
	name nvarchar(70) NOT NULL,
	description text null
)

CREATE TABLE app_status (
	id bigint IDENTITY(1000,1) primary key,
	name nvarchar(70) NOT NULL,
	description text null,
	is_default bit default 0,
	set_active bit default 0
)

CREATE TABLE MusicHub.dbo.album (
	id bigint IDENTITY(1000,1) primary key,
	name nvarchar(70) NOT NULL,
	music_production text NULL,
	music_year int NOT NULL,
	img_url text null,
	img_path text null,
	category_id bigint not null,
	is_active bit default 1 not null,
	status_id bigint not null,
	user_id bigint not null,
	date_new datetime NULL,
	date_edit datetime NULL,
	user_new varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	user_edit varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	CONSTRAINT FK_album__category_id FOREIGN KEY (category_id) REFERENCES MusicHub.dbo.category(id),
	CONSTRAINT FK_album__status FOREIGN KEY (status_id) REFERENCES MusicHub.dbo.app_status(id),
	CONSTRAINT FK_album__app_user FOREIGN KEY (user_id) REFERENCES MusicHub.dbo.app_user(id),
)

CREATE TABLE MusicHub.dbo.album_genre (
	genre_id bigint not null,
	album_id bigint not null,
	CONSTRAINT PK__album_genre__6EDEA1537E812C8D PRIMARY KEY (genre_id, album_id),
	CONSTRAINT FK6hkq1uibwvjsusnnxrsm1gha1 FOREIGN KEY (genre_id) REFERENCES MusicHub.dbo.genre(id),
	CONSTRAINT FKfnlxi1bmv5ao8u3nf3mq7xa12 FOREIGN KEY (album_id) REFERENCES MusicHub.dbo.album(id)
)

CREATE TABLE MusicHub.dbo.album_singer (
	artist_id bigint not null,
	album_id bigint not null,
	CONSTRAINT PK_album_singer__album_id PRIMARY KEY (artist_id, album_id),
	CONSTRAINT FK_album_singer__artist_id FOREIGN KEY (artist_id) REFERENCES MusicHub.dbo.artist(id),
	CONSTRAINT FK_album_singer__album_id FOREIGN KEY (album_id) REFERENCES MusicHub.dbo.album(id)
)

CREATE TABLE MusicHub.dbo.track(
	id bigint IDENTITY(1000,1) primary key,
	name nvarchar(70) NOT NULL,
	album_id bigint null,
	music_production text NULL,
	music_year int null,
	lyric text null,
	description text NULL,
	user_id bigint not null,
	category_id bigint not null,
	status_id bigint not null,
	is_active bit default 1 not null,
	liked bigint default 0,
	listened bigint default 0,
	track_url nvarchar(255) NOT NULL,
	track_path text NOT NULL,
	duration_seconds int NOT NULL,
	bit_rate int NOT NULL,
	date_new datetime NULL,
	date_edit datetime NULL,
	user_new varchar(255) NULL,
	user_edit varchar(255) NULL,
	
	CONSTRAINT FK_track__user_id FOREIGN KEY (user_id) REFERENCES MusicHub.dbo.app_user(id),
	CONSTRAINT FK_track__category_id FOREIGN KEY (category_id) REFERENCES MusicHub.dbo.category(id),
	CONSTRAINT FK_track__status FOREIGN KEY (status_id) REFERENCES MusicHub.dbo.app_status(id),
	CONSTRAINT FK_track__album FOREIGN KEY (album_id) REFERENCES MusicHub.dbo.album(id),
)

CREATE TABLE MusicHub.dbo.track_singer(
	track_id bigint not null,
	artist_id bigint not null,
	CONSTRAINT PK_track_singer PRIMARY KEY (track_id,artist_id),
	CONSTRAINT FK_track_singer__track_id FOREIGN KEY (track_id) REFERENCES MusicHub.dbo.track(id),
	CONSTRAINT FK_track_singer__artist_id FOREIGN KEY (artist_id) REFERENCES MusicHub.dbo.artist(id),
)

CREATE TABLE MusicHub.dbo.track_composer(
	track_id bigint not null,
	artist_id bigint not null,
	CONSTRAINT PK_track_composer PRIMARY KEY (track_id,artist_id),
	CONSTRAINT FK_track_composer__track_id FOREIGN KEY (track_id) REFERENCES MusicHub.dbo.track(id),
	CONSTRAINT FK_track_composer__artist_id FOREIGN KEY (artist_id) REFERENCES MusicHub.dbo.artist(id),
)

CREATE TABLE MusicHub.dbo.track_genre (
	track_id bigint not null,
	genre_id bigint not null,
	CONSTRAINT PK__track_genre PRIMARY KEY (track_id, genre_id),
	CONSTRAINT PK_track_genre__track FOREIGN KEY (track_id) REFERENCES MusicHub.dbo.track(id),
	CONSTRAINT PK_track_genre__genre FOREIGN KEY (genre_id) REFERENCES MusicHub.dbo.genre(id),
)

CREATE TABLE MusicHub.dbo.user_track (
	user_id bigint not null,
	track_id bigint not null,
	date_new datetime null,
	CONSTRAINT PK__user_track PRIMARY KEY (track_id, user_id),
	CONSTRAINT PK_user_track__track FOREIGN KEY (track_id) REFERENCES MusicHub.dbo.track(id),
	CONSTRAINT PK_user_track__user FOREIGN KEY (user_id) REFERENCES MusicHub.dbo.app_user(id)
)

alter table user_track add CONSTRAINT DF_getDateForDateNew DEFAULT GETDATE() FOR date_new

CREATE TABLE playlist_type (
	id bigint IDENTITY(1,1) primary key,
	name nvarchar(70) NOT NULL
)

CREATE TABLE playlist (
	id bigint IDENTITY(1,1) primary key,
	name nvarchar(70) NOT NULL,
	description text null,
	img_url text null,
	img_path text null,
	category_id bigint null,
	genre_id bigint null,
	playlist_type_id bigint null,
	is_public bit default 0,
	liked bigint default 0,
	listened bigint default 0,
	user_id bigint not null,
	date_new datetime NULL,
	date_edit datetime NULL,
	user_new varchar(255) NULL,
	user_edit varchar(255) NULL,
	
	CONSTRAINT PK_playlist__playlist_type FOREIGN KEY (playlist_type_id) REFERENCES MusicHub.dbo.playlist_type(id),
	CONSTRAINT PK_playlist__category FOREIGN KEY (category_id) REFERENCES MusicHub.dbo.category(id),
	CONSTRAINT PK_playlist__genre FOREIGN KEY (genre_id) REFERENCES MusicHub.dbo.genre(id),
	CONSTRAINT PK_playlist__appuser FOREIGN KEY (user_id) REFERENCES MusicHub.dbo.app_user(id),
)

create table playlist_detail (
	id bigint IDENTITY(1,1) primary key,
	playlist_id bigint not null,
	track_id bigint not null,
	date_new datetime null,
	
	CONSTRAINT PK_playlist_detail__playlist FOREIGN KEY (playlist_id) REFERENCES MusicHub.dbo.playlist(id),
	CONSTRAINT PK_playlist_detail__track FOREIGN KEY (track_id) REFERENCES MusicHub.dbo.track(id),
	CONSTRAINT UC_playlist_detail UNIQUE (playlist_id,track_id),
)


INSERT INTO app_role
VALUES('ROLE_ADMIN')

INSERT INTO app_role
VALUES('ROLE_MEMBER')

INSERT INTO app_user 

INSERT INTO genre
VALUES
('Pop music', 'A genre of popular music that originated in the West during the 1950s and 1960s. Pop music is eclectic, often borrowing elements from urban, dance, rock, Latin, country, and other styles. Songs are typically short to medium-length with repeated choruses, melodic tunes, and hooks.'),
('Hip hop music','Hip hop or rap music formed in the United States in the 1970s and consists of stylized rhythmic music that commonly accompanies rhythmic and rhyming speech ("rapping").'),
('Rock music','A genre of popular music that originated as "rock and roll" in the United States in the 1950s, and developed into a range of different styles in the 1960s and later. Compared to pop music, rock places a higher degree of emphasis on musicianship, live performance, and an ideology of authenticity.'),
('Rhythm and blues','A genre of popular African-American music that originated in the 1940s as urbane, rocking, jazz based music with a heavy, insistent beat. Lyrics focus heavily on the themes of triumphs and failures in terms of relationships, freedom, economics, aspirations, and sex.'),
('Soul music','A popular music genre that combines elements of African-American gospel music, rhythm and blues and jazz.'),
('Reggae','A music genre that originated in Jamaica in the late 1960s, strongly influenced by traditional mento as well as American jazz and rhythm and blues, instantly recognizable from the counterpoint between the bass and drum downbeat, and the offbeat rhythm section.'),
('Country','A genre of United States popular music with origins in folk, Blues and Western music, often consisting of ballads and dance tunes with generally simple forms and harmonies accompanied by mostly string instruments such as banjos, electric and acoustic guitars, dobros, and fiddles as well as harmonicas.'),
('Funk','A music genre that originated in the 1960s when African American musicians created a rhythmic, danceable new form of music that de-emphasized melody and chord progressions to bring a strong rhythmic groove of a bass line and drum part to the foreground.'),
('Folk music','A genre that evolved from traditional music during the 20th century folk revival. One meaning often given is that of old songs with no known composers; another is music that has been transmitted and evolved by a process of oral transmission or performed by custom over a long period of time.'),
('Middle Eastern music','Music originating from the vast region from Morocco to Iran, including the Arabic countries of the Middle East and North Africa, the Iraqi traditions of Mesopotamia, Iranian traditions of Persia, the Hebrew music of Israel, Armenian music, the varied traditions of Cypriot music, the music of Turkey, traditional Assyrian music, Berbers of North Africa, and Coptic Christians in Egypt.'),
('Jazz','A music genre that originated from African American communities of New Orleans during the late 19th and early 20th centuries in the form of independent traditional and popular musical styles, all linked by the common bonds of African American and European American musical parentage with a performance orientation.'),
('Disco','A genre of dance music containing elements of funk, soul, pop, and salsa that achieved popularity during the mid-1970s to the early 1980s.'),
('Classical music','Art music produced or rooted in the traditions of Western music, including both liturgical and secular music, over the broad span of time from roughly the 11th century to the present day.'),
('Electronic music','A large set of predominantly popular and dance genres in which synthesizers and other electronic instruments are the primary sources of sound.'),
('Music of Latin America','Music originating from Latin America which encompasses a wide variety of styles, including son, rumba, salsa, merengue, tango, samba, and bossa nova.'),
('Blues','A genre and musical form developed by African Americans in the United States around the end of the 19th century from African-American work songs and European-American folk music. The blues form, ubiquitous in jazz and rock and roll, is characterized by the call-and-response pattern, the blues scale and specific chord progressions, of which the twelve-bar blues is the most common. Blues shuffles or walking bass reinforce the trance-like rhythm and form a repetitive groove effect.'),
('Music for children','Music performed for children, often designed to provide an entertaining means of teaching about cultures, good behavior, facts, and skills.'),
('New-age music','A genre of music intended to create artistic inspiration, relaxation, and optimism used by listeners for yoga, massage, meditation, and reading as a method of stress management or to create a peaceful atmosphere. Includes both electronic and acoustic forms.'),
('Vocal music','Music performed by one or more singers, with or without instrumental accompaniment, in which singing provides the main focus of the piece.'),
('Music of Africa','Music whose style or form clearly indicates African origin or primarily African influence. Given the vastness of the continent, this covers many distinct musical traditions. Sub-Saharan African music frequently relies on percussion instruments including xylophones, drums, and tone-producing instruments such as the mbira or "thumb piano."'),
('Christian music','Music that has been written to express either personal or a communal belief regarding Christian life and faith. Its forms vary widely across the world, according to culture and social context.'),
('Music of Asia','Musical styles originating from a large number of Asian countries located in Central, Southern, and East Asia.'),
('Ska','A music genre that originated in Jamaica in the late 1950s, combining elements of Caribbean mento and calypso with American jazz and rhythm and blues. It is characterized by a walking bass line accented with rhythms on the off-beat.'),
('Traditional music','Musical forms that have origins many generations into the past, commonly without formal notation or description, commonly familiar to people in a given culture.'),
('Independent music','Music produced independent of major commercial record labels, possibly including a do-it-yourself approach to recording and publishing. The term indie is also used to describe music of this style regardless of actual production channel.')

INSERT INTO category(name)
VALUES('Beat, Playback'), (N'Nhạc Việt Nam'), (N'Nhạc US-UK'), (N'Nhạc Hoa'), (N'Nhạc Hàn'), (N'Nhạc Nhật'), (N'Nhạc Pháp'), (N'Nhạc nước khác')

INSERT INTO app_status
(name, description, is_default)
VALUES(N'Chờ duyệt', NULL, 1), (N'Đã duyệt', NULL, 0)


-- alter table track alter column name nvarchar(70) NOT NULL
-- alter table track add duration_seconds int NOT NULL default 0
-- alter table track alter column duration_seconds int NOT NULL
-- alter table track add bit_rate int NOT NULL default 0
-- alter table track alter column bit_rate int NOT NULL
-- alter table playlist_detail add CONSTRAINT PK_playlist_detail__track FOREIGN KEY (track_id) REFERENCES MusicHub.dbo.track(id)
