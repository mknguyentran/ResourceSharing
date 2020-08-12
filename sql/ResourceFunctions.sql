Create Function RESOURCECATEGORYNAME(@id int)
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
