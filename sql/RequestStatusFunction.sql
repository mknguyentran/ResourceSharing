Create Function REQUEST_STATUS_NAME(@statusID int)
Returns varchar(10)
AS
BEGIN
	RETURN
	(
		Select Name
		From RequestStatus
		Where ID = @statusID
	)
END