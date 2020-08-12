Create Function ACCOUNTSTATUSID(@statusName varchar(10))
Returns int
AS
BEGIN
	DECLARE @result int

	SET @result = (
		Select ID
		From AccountStatus
		Where Name = @statusName
	)
	RETURN @result;
END

Create Function ACCOUNTSTATUSNAME(@statusID int)
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

Create Function ACCOUNTROLEID(@roleName varchar(10))
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

Create Function ACCOUNTROLENAME(@roleID int)
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

Drop function TOTAL