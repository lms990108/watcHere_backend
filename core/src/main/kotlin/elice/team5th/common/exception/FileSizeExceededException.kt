package elice.team5th.common.exception

class FileSizeExceededException(detail: String) : InvalidRequestException(ErrorType.FILE_SIZE_EXCEEDED, detail)
