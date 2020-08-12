Create Function CHECK_RESOURCE_OCCUPIED_AMOUNT_IN_RANGE(@resourceID int, @fromDate datetime, @toDate dateTime)
Returns int
AS
BEGIN
	DECLARE @result int
	Set @result = (
		Select Sum(Amount)
		From RequestDetail
		Where RequestID in (Select ID From dbo.GET_INTERFERING_REQUESTS(@fromDate,@toDate)) And ResourceID = @resourceID
	)
	Return @result
END

Create Function CHECK_RESOURCE_AVAILABLE_AMOUNT_IN_RANGE(@resourceID int, @fromDate datetime, @toDate dateTime)
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

Select dbo.CHECK_RESOURCE_AVAILABLE_AMOUNT_IN_RANGE(2, '2020-07-10 00:00:00.000','2020-07-10 00:00:00.000')
Select dbo.CHECK_RESOURCE_OCCUPIED_AMOUNT_IN_RANGE(2, '2020-07-10 00:00:00.000','2020-07-10 00:00:00.000')
Select dbo.CHECK_RESOURCE_OCCUPIED_AMOUNT(2, '2020-07-10 00:00:00.000')