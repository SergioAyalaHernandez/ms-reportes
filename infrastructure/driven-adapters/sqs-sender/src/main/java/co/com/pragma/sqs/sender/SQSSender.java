package co.com.pragma.sqs.sender;

import co.com.pragma.model.gateway.ReportSQSGateway;
import co.com.pragma.sqs.sender.config.SQSSenderProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@Log4j2

public class SQSSender implements ReportSQSGateway {
  private final SQSSenderProperties properties;

  private final SqsAsyncClient client;

  public SQSSender(SQSSenderProperties properties, @Qualifier("configSenderSqs") SqsAsyncClient client) {
    this.properties = properties;
    this.client = client;
  }

  @Override
  public Mono<Void> emit(String message) {
    return Mono.fromCallable(() -> buildRequest(message))
            .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
            .doOnNext(response -> log.debug(response.messageId()))
            .then();
  }

  private SendMessageRequest buildRequest(String message) {
    return SendMessageRequest.builder()
            .queueUrl(properties.queueUrl())
            .messageBody(message)
            .build();
  }

}
