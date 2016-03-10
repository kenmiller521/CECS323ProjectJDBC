
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  ken_m
 * Created: Mar 9, 2016
 */

CREATE TABLE recordinggroups
(
    GroupName VARCHAR(45) NOT NULL,
    LeadSinger VARCHAR(60),
    YearFormed Integer,
    Genre VARCHAR(60),
    CONSTRAINT pk_recordinggroups PRIMARY KEY (GroupName)
);

CREATE TABLE albums(
    AlbumTitle VARCHAR(60) NOT NULL,
    GroupName VARCHAR(45) NOT NULL,
    NumberOfSongs integer,
    StudioName VARCHAR(50),
    DateRecorded DATE,
    Length integer,
    CONSTRAINT pk_albums PRIMARY KEY (AlbumTitle, GroupName)
    
);

CREATE TABLE recordingstudios(
    StudioName VARCHAR(50) NOT NULL,
    StudioAddress VARCHAR(100),
    StudioOwner VARCHAR(60),
    StudioPhone VARCHAR(13),
    CONSTRAINT pk_recordingstudios PRIMARY KEY (StudioName)
);

ALTER TABLE albums
    ADD CONSTRAINT fk_albums1
    FOREIGN KEY (StudioName)
    REFERENCES recordingstudios(StudioName);

ALTER TABLE albums
    ADD CONSTRAINT fk_albums2
    FOREIGN KEY (GroupName)
    REFERENCES recordinggroups(GroupName);


