Create Function GET_INTERFERING_REQUESTS(@fromDate datetime, @toDate datetime)
Returns @result TABLE
(
	ID int PRIMARY KEY NOT NULL
)
AS
BEGIN
	INSERT @result
		Select ID
		From Request
		Where (FromDate <= @fromDate AND ToDate >= @toDate) OR (FromDate <= @fromDate AND ToDate <= @toDate AND ToDate >= @fromDate) OR (FromDate >= @fromDate AND FromDate <= @toDate AND ToDate >= @toDate) OR (FromDate >= @fromDate AND ToDate <= @toDate)
	RETURN
END