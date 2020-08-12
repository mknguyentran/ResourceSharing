Create Function CHECK_VERIFICATION_CODE(@email varchar(50), @code char(4))
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