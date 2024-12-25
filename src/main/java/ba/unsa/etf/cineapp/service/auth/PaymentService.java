package ba.unsa.etf.cineapp.service.auth;

import ba.unsa.etf.cineapp.dto.request.auth.PaymentRequest;
import ba.unsa.etf.cineapp.dto.response.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  @Value("${stripe.secret.api.key}")
  private String secretKey;

  @PostConstruct
  public void init() {
    Stripe.apiKey = secretKey;
  }

  public PaymentResponse createPaymentIntent(final PaymentRequest paymentRequest) throws StripeException {
    PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
        .setAmount((long) (paymentRequest.getAmount() * 100)) // amount in cents
        .setCurrency("bam")
        .addPaymentMethodType("card")
        .build();

    PaymentIntent intent = PaymentIntent.create(params);

    PaymentResponse paymentResponse = new PaymentResponse();
    paymentResponse.setClientSecret(intent.getClientSecret());
    return paymentResponse;
  }
}
