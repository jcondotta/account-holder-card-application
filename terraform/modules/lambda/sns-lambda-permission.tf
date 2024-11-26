resource "aws_lambda_permission" "this" {
  statement_id  = "AllowExecutionFromSNS"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.account_holder_card_application_lambda.function_name
  principal     = "sns.amazonaws.com"
  source_arn    = var.sns_bank_account_created_topic_arn
}