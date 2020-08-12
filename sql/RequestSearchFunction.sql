Create Function SEARCH_REQUEST_BY_RESOURCE_NAME(@searchName varchar(50))
Returns @result TABLE
(
	ID int PRIMARY KEY NOT NULL
)
AS
BEGIN
	INSERT @result
		Select RequestID
		From RequestDetail
		Where ResourceID in 
		(
			Select ID
			From Resource
			Where Name LIKE '%' + @searchName + '%'
		)
	RETURN
END