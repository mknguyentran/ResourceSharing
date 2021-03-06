USE [master]
GO
/****** Object:  Database [ResourceSharing]    Script Date: 7/19/2020 10:25:09 PM ******/
CREATE DATABASE [ResourceSharing]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'ResourceSharing', FILENAME = N'D:\rdsdbdata\DATA\ResourceSharing.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 10%)
 LOG ON 
( NAME = N'ResourceSharing_log', FILENAME = N'D:\rdsdbdata\DATA\ResourceSharing_log.ldf' , SIZE = 1024KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [ResourceSharing].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [ResourceSharing] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [ResourceSharing] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [ResourceSharing] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [ResourceSharing] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [ResourceSharing] SET ARITHABORT OFF 
GO
ALTER DATABASE [ResourceSharing] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [ResourceSharing] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [ResourceSharing] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [ResourceSharing] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [ResourceSharing] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [ResourceSharing] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [ResourceSharing] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [ResourceSharing] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [ResourceSharing] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [ResourceSharing] SET  DISABLE_BROKER 
GO
ALTER DATABASE [ResourceSharing] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [ResourceSharing] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [ResourceSharing] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [ResourceSharing] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [ResourceSharing] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [ResourceSharing] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [ResourceSharing] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [ResourceSharing] SET RECOVERY FULL 
GO
ALTER DATABASE [ResourceSharing] SET  MULTI_USER 
GO
ALTER DATABASE [ResourceSharing] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [ResourceSharing] SET DB_CHAINING OFF 
GO
ALTER DATABASE [ResourceSharing] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [ResourceSharing] SET TARGET_RECOVERY_TIME = 0 SECONDS 
GO
ALTER DATABASE [ResourceSharing] SET DELAYED_DURABILITY = DISABLED 
GO
USE [ResourceSharing]
GO
/****** Object:  User [mkngtrn]    Script Date: 7/19/2020 10:25:10 PM ******/
CREATE USER [mkngtrn] FOR LOGIN [mkngtrn] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_owner] ADD MEMBER [mkngtrn]
GO
/****** Object:  UserDefinedFunction [dbo].[ACCOUNTROLEID]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
Create Function [dbo].[ACCOUNTROLEID](@roleName varchar(10))
Returns int
AS
BEGIN
	DECLARE @result int

	SET @result = (
		Select ID
		From Role
		Where Name = @roleName
	)
	RETURN @result;
END
GO
/****** Object:  UserDefinedFunction [dbo].[ACCOUNTROLENAME]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
Create Function [dbo].[ACCOUNTROLENAME](@roleID int)
Returns varchar(10)
AS
BEGIN
	DECLARE @result varchar(10)

	SET @result = (
		Select Name
		From Role
		Where id = @roleID
	)
	RETURN @result;
END
GO
/****** Object:  UserDefinedFunction [dbo].[ACCOUNTSTATUSID]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
Create Function [dbo].[ACCOUNTSTATUSID](@statusName varchar(10))
Returns int
AS
BEGIN
	DECLARE @result int
	DECLARE @discountPercentage int
	DECLARE @discountCode varchar(30)

	SET @result = (
		Select ID
		From AccountStatus
		Where Name = @statusName
	)
	RETURN @result;
END
GO
/****** Object:  UserDefinedFunction [dbo].[ACCOUNTSTATUSNAME]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
Create Function [dbo].[ACCOUNTSTATUSNAME](@statusID int)
Returns varchar(10)
AS
BEGIN
	DECLARE @result varchar(10)

	SET @result = (
		Select Name
		From AccountStatus
		Where id = @statusID
	)
	RETURN @result;
END
GO
/****** Object:  UserDefinedFunction [dbo].[CHECK_RESOURCE_AVAILABLE_AMOUNT]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
Create Function [dbo].[CHECK_RESOURCE_AVAILABLE_AMOUNT](@resourceID int, @date datetime)
Returns int
AS
BEGIN
	DECLARE @result int
	DECLARE @quantity int
	DECLARE @occupiedAmount int
	Set @quantity = (
		Select Quantity
		From Resource
		Where ID = @resourceID
	)
	IF @quantity IS NOT NULL
	BEGIN
		Set @result = @quantity - dbo.CHECK_RESOURCE_OCCUPIED_AMOUNT(@resourceID, @date)
	END

	ELSE
	BEGIN
		Set @result = -1
	END
	return @result
END
GO
/****** Object:  UserDefinedFunction [dbo].[CHECK_RESOURCE_AVAILABLE_AMOUNT_IN_RANGE]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
Create Function [dbo].[CHECK_RESOURCE_AVAILABLE_AMOUNT_IN_RANGE](@resourceID int, @fromDate datetime, @toDate dateTime)
Returns int
AS
BEGIN
	DECLARE @result int
	DECLARE @quantity int
	Set @quantity = (
		Select Quantity
		From Resource
		Where ID = @resourceID
	)
	Set @result = @quantity - dbo.CHECK_RESOURCE_OCCUPIED_AMOUNT_IN_RANGE(@resourceID,@fromDate,@toDate)
	Return @result
END
GO
/****** Object:  UserDefinedFunction [dbo].[CHECK_RESOURCE_OCCUPIED_AMOUNT]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE Function [dbo].[CHECK_RESOURCE_OCCUPIED_AMOUNT](@resourceID int, @date datetime)
Returns int
AS
BEGIN
	DECLARE @result int
	Set @result = (
		Select SUM(Amount)
		From RequestDetail
		Where RequestID in (
			Select ID
			From Request
			Where FromDate <= @date AND ToDate >= @date AND Status = 2
		) AND ResourceID = @resourceID
	)
	IF @result IS NULL
	BEGIN
		Set @result = 0
	END
	return @result
END
GO
/****** Object:  UserDefinedFunction [dbo].[CHECK_RESOURCE_OCCUPIED_AMOUNT_IN_RANGE]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE Function [dbo].[CHECK_RESOURCE_OCCUPIED_AMOUNT_IN_RANGE](@resourceID int, @fromDate datetime, @toDate dateTime)
Returns int
AS
BEGIN
	DECLARE @result int
	Set @result = (
		Select Sum(Amount)
		From RequestDetail
		Where RequestID in (Select ID From dbo.GET_INTERFERING_REQUESTS(@fromDate,@toDate)) And ResourceID = @resourceID
	)
	IF @result Is Null
	BEGIN
		Set @result = 0
	END
	Return @result
END
GO
/****** Object:  UserDefinedFunction [dbo].[CHECK_VERIFICATION_CODE]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
Create Function [dbo].[CHECK_VERIFICATION_CODE](@email varchar(50), @code char(4))
Returns bit
BEGIN
	DECLARE @result bit
	DECLARE @currentCode char(4)
	Set @currentCode = (
		Select TOP 1 VerificationCode
		From VerificationCode
		Where AccountEmail = @email AND DATEDIFF(mi,CreatedAt,CURRENT_TIMESTAMP) <= 5
		Order By CreatedAt DESC
	)
	IF @currentCode IS NOT NULL
	BEGIN
		IF @currentCode = @code
		BEGIN
			Set @result = 1
		END
		ELSE
		BEGIN
			Set @result = 0
		END
	END
	ELSE
	BEGIN
		Set @result = 0
	END
	RETURN @result
END
GO
/****** Object:  UserDefinedFunction [dbo].[GET_INTERFERING_REQUESTS]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE Function [dbo].[GET_INTERFERING_REQUESTS](@fromDate datetime, @toDate datetime)
Returns @result TABLE
(
	ID int PRIMARY KEY NOT NULL
)
AS
BEGIN
	INSERT @result
		Select ID
		From Request
		Where Status = 2 AND ((FromDate <= @fromDate AND ToDate >= @toDate) OR (FromDate <= @fromDate AND ToDate <= @toDate AND ToDate >= @fromDate) OR (FromDate >= @fromDate AND FromDate <= @toDate AND ToDate >= @toDate) OR (FromDate >= @fromDate AND ToDate <= @toDate))
	RETURN
END
GO
/****** Object:  UserDefinedFunction [dbo].[REQUEST_STATUS_NAME]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
Create Function [dbo].[REQUEST_STATUS_NAME](@statusID int)
Returns varchar(10)
AS
BEGIN
	RETURN(
	Select Name
	From RequestStatus
	Where ID = @statusID
	)
END
GO
/****** Object:  UserDefinedFunction [dbo].[RESOURCECATEGORYNAME]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
Create Function [dbo].[RESOURCECATEGORYNAME](@id int)
Returns varchar(10)
AS
BEGIN
	DECLARE @result varchar(10)	
	Set @result = (
		Select Name
		From ResourceCategory
		Where ID = @id
	)
	Return @result
END
GO
/****** Object:  UserDefinedFunction [dbo].[SEARCH_REQUEST_BY_RESOURCE_NAME]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE Function [dbo].[SEARCH_REQUEST_BY_RESOURCE_NAME](@searchName varchar(50))
Returns @result TABLE
(
	ID int PRIMARY KEY NOT NULL
)
AS
BEGIN
	INSERT @result
		Select DISTINCT RequestID
		From RequestDetail
		Where ResourceID in 
		(
			Select ID
			From Resource
			Where Name LIKE '%' + @searchName + '%'
		)
	RETURN
END
GO
/****** Object:  Table [dbo].[Account]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Account](
	[Email] [varchar](50) NOT NULL,
	[Password] [varchar](30) NULL,
	[Name] [nvarchar](50) NULL,
	[PhoneNumber] [varchar](20) NULL,
	[Address] [nvarchar](100) NULL,
	[Role] [int] NULL,
	[CreatedAt] [datetime] NULL,
	[Status] [int] NULL,
 CONSTRAINT [PK_Account] PRIMARY KEY CLUSTERED 
(
	[Email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[AccountStatus]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AccountStatus](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](10) NULL,
 CONSTRAINT [PK_AccountStatus] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Request]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Request](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[SentAt] [datetimeoffset](7) NULL,
	[FromDate] [date] NULL,
	[ToDate] [date] NULL,
	[RequestedUser] [varchar](50) NULL,
	[ProcessedManager] [varchar](50) NULL,
	[Status] [int] NULL CONSTRAINT [DF_Request_Status]  DEFAULT ((1)),
 CONSTRAINT [PK_Request] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[RequestDetail]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RequestDetail](
	[RequestID] [int] NOT NULL,
	[ResourceID] [int] NOT NULL,
	[Amount] [int] NULL,
 CONSTRAINT [PK_RequestDetail] PRIMARY KEY CLUSTERED 
(
	[RequestID] ASC,
	[ResourceID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[RequestStatus]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RequestStatus](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](10) NULL,
 CONSTRAINT [PK_RequestStatus] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Resource]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Resource](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](30) NULL,
	[Category] [int] NULL,
	[Quantity] [int] NULL,
	[Color] [varchar](10) NULL,
	[RequestAuthority] [int] NULL,
 CONSTRAINT [PK_Resource] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ResourceCategory]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ResourceCategory](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](10) NULL,
 CONSTRAINT [PK_ResourceStatus] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Role]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Role](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](10) NULL,
 CONSTRAINT [PK_Role] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[VerificationCode]    Script Date: 7/19/2020 10:25:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[VerificationCode](
	[AccountEmail] [varchar](50) NOT NULL,
	[VerificationCode] [char](4) NULL,
	[CreatedAt] [datetime] NOT NULL,
 CONSTRAINT [PK_VerificationCode] PRIMARY KEY CLUSTERED 
(
	[AccountEmail] ASC,
	[CreatedAt] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
INSERT [dbo].[Account] ([Email], [Password], [Name], [PhoneNumber], [Address], [Role], [CreatedAt], [Status]) VALUES (N'employee@gmail.com', N'123', N'Trần Quang Thông', N'+84032345678', N'55/3A Phan Van Tri, Ward 4, Binh Thanh District', 3, CAST(N'2020-06-27 00:00:00.000' AS DateTime), 2)
INSERT [dbo].[Account] ([Email], [Password], [Name], [PhoneNumber], [Address], [Role], [CreatedAt], [Status]) VALUES (N'khoantmse140741@fpt.edu.vn', N'ZXBwbHVpNDBvM3V0N2hyLmFwcHMuZ2', N'Nguyen Tran Minh Khoa (K14 HCM)', NULL, NULL, 3, CAST(N'2020-07-03 04:23:02.857' AS DateTime), 2)
INSERT [dbo].[Account] ([Email], [Password], [Name], [PhoneNumber], [Address], [Role], [CreatedAt], [Status]) VALUES (N'leader@gmail.com', N'123', N'Lê Ngọc Di', N'+84944123456', N'25 Phan Huy Ich, Ward 5, District 12', 2, CAST(N'2020-06-28 00:00:00.000' AS DateTime), 2)
INSERT [dbo].[Account] ([Email], [Password], [Name], [PhoneNumber], [Address], [Role], [CreatedAt], [Status]) VALUES (N'manager@gmail.com', N'123', N'Ngô Minh Châu', N'+84901234567', N'1 Hoang Van Thu, Ward 4, Tan Binh District', 1, CAST(N'2020-06-29 00:00:00.000' AS DateTime), 2)
INSERT [dbo].[Account] ([Email], [Password], [Name], [PhoneNumber], [Address], [Role], [CreatedAt], [Status]) VALUES (N'mknguyentran@yahoo.com', N'123', N'Khoa', NULL, NULL, 3, CAST(N'2020-07-03 04:06:22.513' AS DateTime), 2)
INSERT [dbo].[Account] ([Email], [Password], [Name], [PhoneNumber], [Address], [Role], [CreatedAt], [Status]) VALUES (N'tam.neighbors59@gmail.com', N'123', N'GL', N'0944800560', N'70 Nguyen Sy Sach', 3, CAST(N'2020-07-19 15:04:36.913' AS DateTime), 2)
SET IDENTITY_INSERT [dbo].[AccountStatus] ON 

INSERT [dbo].[AccountStatus] ([ID], [Name]) VALUES (1, N'new')
INSERT [dbo].[AccountStatus] ([ID], [Name]) VALUES (2, N'active')
INSERT [dbo].[AccountStatus] ([ID], [Name]) VALUES (3, N'deactive')
SET IDENTITY_INSERT [dbo].[AccountStatus] OFF
SET IDENTITY_INSERT [dbo].[Request] ON 

INSERT [dbo].[Request] ([ID], [SentAt], [FromDate], [ToDate], [RequestedUser], [ProcessedManager], [Status]) VALUES (1, CAST(N'2020-07-09T00:00:00.0000000+00:00' AS DateTimeOffset), CAST(N'2020-07-09' AS Date), CAST(N'2020-07-12' AS Date), N'employee@gmail.com', N'manager@gmail.com', 2)
INSERT [dbo].[Request] ([ID], [SentAt], [FromDate], [ToDate], [RequestedUser], [ProcessedManager], [Status]) VALUES (2, CAST(N'2020-07-08T00:00:00.0000000+00:00' AS DateTimeOffset), CAST(N'2020-07-09' AS Date), CAST(N'2020-07-11' AS Date), N'leader@gmail.com', N'manager@gmail.com', 2)
INSERT [dbo].[Request] ([ID], [SentAt], [FromDate], [ToDate], [RequestedUser], [ProcessedManager], [Status]) VALUES (3, CAST(N'2020-07-18T13:58:19.1800000+00:00' AS DateTimeOffset), CAST(N'2020-07-23' AS Date), CAST(N'2020-07-30' AS Date), N'khoantmse140741@fpt.edu.vn', N'manager@gmail.com', 2)
INSERT [dbo].[Request] ([ID], [SentAt], [FromDate], [ToDate], [RequestedUser], [ProcessedManager], [Status]) VALUES (4, CAST(N'2020-07-18T13:59:49.7400000+00:00' AS DateTimeOffset), CAST(N'2020-07-22' AS Date), CAST(N'2020-07-24' AS Date), N'khoantmse140741@fpt.edu.vn', NULL, 4)
INSERT [dbo].[Request] ([ID], [SentAt], [FromDate], [ToDate], [RequestedUser], [ProcessedManager], [Status]) VALUES (5, CAST(N'2020-07-19T05:45:13.5633333+00:00' AS DateTimeOffset), CAST(N'2020-07-20' AS Date), CAST(N'2020-07-20' AS Date), N'khoantmse140741@fpt.edu.vn', NULL, 1)
INSERT [dbo].[Request] ([ID], [SentAt], [FromDate], [ToDate], [RequestedUser], [ProcessedManager], [Status]) VALUES (6, CAST(N'2020-07-19T06:58:25.5200000+00:00' AS DateTimeOffset), CAST(N'2020-07-21' AS Date), CAST(N'2020-07-22' AS Date), N'mknguyentran@yahoo.com', N'manager@gmail.com', 2)
INSERT [dbo].[Request] ([ID], [SentAt], [FromDate], [ToDate], [RequestedUser], [ProcessedManager], [Status]) VALUES (7, CAST(N'2020-07-19T12:17:47.3066667+00:00' AS DateTimeOffset), CAST(N'2020-07-20' AS Date), CAST(N'2020-07-21' AS Date), N'mknguyentran@yahoo.com', N'manager@gmail.com', 2)
INSERT [dbo].[Request] ([ID], [SentAt], [FromDate], [ToDate], [RequestedUser], [ProcessedManager], [Status]) VALUES (8, CAST(N'2020-07-19T12:18:19.2266667+00:00' AS DateTimeOffset), CAST(N'2020-07-20' AS Date), CAST(N'2020-07-23' AS Date), N'khoantmse140741@fpt.edu.vn', N'manager@gmail.com', 3)
INSERT [dbo].[Request] ([ID], [SentAt], [FromDate], [ToDate], [RequestedUser], [ProcessedManager], [Status]) VALUES (9, CAST(N'2020-07-19T15:09:50.1566667+00:00' AS DateTimeOffset), CAST(N'2020-07-22' AS Date), CAST(N'2020-07-24' AS Date), N'tam.neighbors59@gmail.com', NULL, 4)
INSERT [dbo].[Request] ([ID], [SentAt], [FromDate], [ToDate], [RequestedUser], [ProcessedManager], [Status]) VALUES (10, CAST(N'2020-07-19T15:11:06.3400000+00:00' AS DateTimeOffset), CAST(N'2020-07-22' AS Date), CAST(N'2020-07-22' AS Date), N'tam.neighbors59@gmail.com', NULL, 1)
INSERT [dbo].[Request] ([ID], [SentAt], [FromDate], [ToDate], [RequestedUser], [ProcessedManager], [Status]) VALUES (11, CAST(N'2020-07-19T15:12:05.5100000+00:00' AS DateTimeOffset), CAST(N'2020-07-22' AS Date), CAST(N'2020-07-24' AS Date), N'tam.neighbors59@gmail.com', N'manager@gmail.com', 2)
SET IDENTITY_INSERT [dbo].[Request] OFF
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (1, 2, 5)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (1, 4, 1)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (2, 1, 1)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (2, 2, 30)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (3, 7, 1)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (3, 10, 1)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (4, 7, 3)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (4, 8, 2)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (4, 9, 1)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (5, 2, 5)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (5, 7, 1)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (6, 7, 1)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (7, 10, 3)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (8, 10, 2)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (9, 4, 7)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (10, 7, 4)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (10, 8, 2)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (10, 10, 1)
INSERT [dbo].[RequestDetail] ([RequestID], [ResourceID], [Amount]) VALUES (11, 8, 5)
SET IDENTITY_INSERT [dbo].[RequestStatus] ON 

INSERT [dbo].[RequestStatus] ([ID], [Name]) VALUES (1, N'new')
INSERT [dbo].[RequestStatus] ([ID], [Name]) VALUES (2, N'accept')
INSERT [dbo].[RequestStatus] ([ID], [Name]) VALUES (3, N'delete')
INSERT [dbo].[RequestStatus] ([ID], [Name]) VALUES (4, N'inactive')
SET IDENTITY_INSERT [dbo].[RequestStatus] OFF
SET IDENTITY_INSERT [dbo].[Resource] ON 

INSERT [dbo].[Resource] ([ID], [Name], [Category], [Quantity], [Color], [RequestAuthority]) VALUES (1, N'HP Projector', 3, 5, N'gray', 2)
INSERT [dbo].[Resource] ([ID], [Name], [Category], [Quantity], [Color], [RequestAuthority]) VALUES (2, N'Plastic Chair', 2, 35, N'blue', 3)
INSERT [dbo].[Resource] ([ID], [Name], [Category], [Quantity], [Color], [RequestAuthority]) VALUES (3, N'Room 001', 1, 1, NULL, 2)
INSERT [dbo].[Resource] ([ID], [Name], [Category], [Quantity], [Color], [RequestAuthority]) VALUES (4, N'HDMI Cable', 6, 7, N'black', 3)
INSERT [dbo].[Resource] ([ID], [Name], [Category], [Quantity], [Color], [RequestAuthority]) VALUES (5, N'Room 002', 1, 1, NULL, 2)
INSERT [dbo].[Resource] ([ID], [Name], [Category], [Quantity], [Color], [RequestAuthority]) VALUES (6, N'Large Speaker', 4, 3, N'black', 2)
INSERT [dbo].[Resource] ([ID], [Name], [Category], [Quantity], [Color], [RequestAuthority]) VALUES (7, N'Portable Speaker', 4, 5, N'orange', 3)
INSERT [dbo].[Resource] ([ID], [Name], [Category], [Quantity], [Color], [RequestAuthority]) VALUES (8, N'Dell Laptop', 5, 5, N'gray', 3)
INSERT [dbo].[Resource] ([ID], [Name], [Category], [Quantity], [Color], [RequestAuthority]) VALUES (9, N'Round Table', 7, 6, N'brown', 3)
INSERT [dbo].[Resource] ([ID], [Name], [Category], [Quantity], [Color], [RequestAuthority]) VALUES (10, N'Audio 3.5mm Cable', 6, 4, N'black', 3)
SET IDENTITY_INSERT [dbo].[Resource] OFF
SET IDENTITY_INSERT [dbo].[ResourceCategory] ON 

INSERT [dbo].[ResourceCategory] ([ID], [Name]) VALUES (1, N'room')
INSERT [dbo].[ResourceCategory] ([ID], [Name]) VALUES (2, N'chair')
INSERT [dbo].[ResourceCategory] ([ID], [Name]) VALUES (3, N'projector')
INSERT [dbo].[ResourceCategory] ([ID], [Name]) VALUES (4, N'speaker')
INSERT [dbo].[ResourceCategory] ([ID], [Name]) VALUES (5, N'computer')
INSERT [dbo].[ResourceCategory] ([ID], [Name]) VALUES (6, N'cable')
INSERT [dbo].[ResourceCategory] ([ID], [Name]) VALUES (7, N'desk')
SET IDENTITY_INSERT [dbo].[ResourceCategory] OFF
SET IDENTITY_INSERT [dbo].[Role] ON 

INSERT [dbo].[Role] ([ID], [Name]) VALUES (1, N'manager')
INSERT [dbo].[Role] ([ID], [Name]) VALUES (2, N'leader')
INSERT [dbo].[Role] ([ID], [Name]) VALUES (3, N'employee')
SET IDENTITY_INSERT [dbo].[Role] OFF
INSERT [dbo].[VerificationCode] ([AccountEmail], [VerificationCode], [CreatedAt]) VALUES (N'mknguyentran@yahoo.com', N'1908', CAST(N'2020-07-19 14:48:26.387' AS DateTime))
INSERT [dbo].[VerificationCode] ([AccountEmail], [VerificationCode], [CreatedAt]) VALUES (N'mknguyentran@yahoo.com', N'0161', CAST(N'2020-07-19 14:56:42.960' AS DateTime))
INSERT [dbo].[VerificationCode] ([AccountEmail], [VerificationCode], [CreatedAt]) VALUES (N'tam.neighbors59@gmail.com', N'4480', CAST(N'2020-07-19 15:04:40.933' AS DateTime))
ALTER TABLE [dbo].[Account]  WITH CHECK ADD  CONSTRAINT [FK_Account_AccountStatus] FOREIGN KEY([Status])
REFERENCES [dbo].[AccountStatus] ([ID])
GO
ALTER TABLE [dbo].[Account] CHECK CONSTRAINT [FK_Account_AccountStatus]
GO
ALTER TABLE [dbo].[Account]  WITH CHECK ADD  CONSTRAINT [FK_Account_Role] FOREIGN KEY([Role])
REFERENCES [dbo].[Role] ([ID])
GO
ALTER TABLE [dbo].[Account] CHECK CONSTRAINT [FK_Account_Role]
GO
ALTER TABLE [dbo].[Request]  WITH CHECK ADD  CONSTRAINT [FK_Request_Account] FOREIGN KEY([RequestedUser])
REFERENCES [dbo].[Account] ([Email])
GO
ALTER TABLE [dbo].[Request] CHECK CONSTRAINT [FK_Request_Account]
GO
ALTER TABLE [dbo].[Request]  WITH CHECK ADD  CONSTRAINT [FK_Request_RequestStatus] FOREIGN KEY([Status])
REFERENCES [dbo].[RequestStatus] ([ID])
GO
ALTER TABLE [dbo].[Request] CHECK CONSTRAINT [FK_Request_RequestStatus]
GO
ALTER TABLE [dbo].[RequestDetail]  WITH CHECK ADD  CONSTRAINT [FK_RequestDetail_Request] FOREIGN KEY([RequestID])
REFERENCES [dbo].[Request] ([ID])
GO
ALTER TABLE [dbo].[RequestDetail] CHECK CONSTRAINT [FK_RequestDetail_Request]
GO
ALTER TABLE [dbo].[RequestDetail]  WITH CHECK ADD  CONSTRAINT [FK_RequestDetail_Resource] FOREIGN KEY([ResourceID])
REFERENCES [dbo].[Resource] ([ID])
GO
ALTER TABLE [dbo].[RequestDetail] CHECK CONSTRAINT [FK_RequestDetail_Resource]
GO
ALTER TABLE [dbo].[Resource]  WITH CHECK ADD  CONSTRAINT [FK_Resource_ResourceCategory] FOREIGN KEY([Category])
REFERENCES [dbo].[ResourceCategory] ([ID])
GO
ALTER TABLE [dbo].[Resource] CHECK CONSTRAINT [FK_Resource_ResourceCategory]
GO
ALTER TABLE [dbo].[Resource]  WITH CHECK ADD  CONSTRAINT [FK_Resource_Role] FOREIGN KEY([RequestAuthority])
REFERENCES [dbo].[Role] ([ID])
GO
ALTER TABLE [dbo].[Resource] CHECK CONSTRAINT [FK_Resource_Role]
GO
ALTER TABLE [dbo].[VerificationCode]  WITH CHECK ADD  CONSTRAINT [FK_VerificationCode_Account] FOREIGN KEY([AccountEmail])
REFERENCES [dbo].[Account] ([Email])
GO
ALTER TABLE [dbo].[VerificationCode] CHECK CONSTRAINT [FK_VerificationCode_Account]
GO
USE [master]
GO
ALTER DATABASE [ResourceSharing] SET  READ_WRITE 
GO
