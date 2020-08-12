Create Function CHECK_RESOURCE_AVAILABLE_AMOUNT(@resourceID int, @date datetime)
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

Create Function CHECK_RESOURCE_OCCUPIED_AMOUNT(@resourceID int, @date datetime)
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
			Where FromDate <= CURRENT_TIMESTAMP AND ToDate >= CURRENT_TIMESTAMP AND Status = 2
		) AND ResourceID = @resourceID
	)
	IF @result IS NULL
	BEGIN
		Set @result = -1
	END
	return @result
END
