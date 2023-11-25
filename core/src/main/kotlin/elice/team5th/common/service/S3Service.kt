package elice.team5th.common.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class S3Service(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    private val amazonS3: AmazonS3
) {
    fun uploadFile(multipartFile: MultipartFile): String {
        val originalFileName = multipartFile.originalFilename

        val metadata = ObjectMetadata()
        metadata.contentType = multipartFile.contentType
        metadata.contentLength = multipartFile.size

        amazonS3.putObject(bucket, originalFileName, multipartFile.inputStream, metadata)
        return amazonS3.getUrl(bucket, originalFileName).toString()
    }
}
