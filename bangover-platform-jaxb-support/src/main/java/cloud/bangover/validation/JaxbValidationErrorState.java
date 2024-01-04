package cloud.bangover.validation;

import cloud.bangover.CollectionWrapper;
import cloud.bangover.text.Text;
import cloud.bangover.text.TextTemplates;
import cloud.bangover.validation.ValidationState.ErrorState;
import cloud.bangover.validation.ValidationState.GroupedError;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ValidationErrors")
public class JaxbValidationErrorState implements ErrorState {

  @XmlElement
  private List<String> ungroupedErrors = new ArrayList<>();

  @XmlElementWrapper
  @XmlElement(name = "groupedError")
  private List<JaxbGroupedError> groupedErrors = new ArrayList<>();

  public JaxbValidationErrorState(ErrorState errorState) {
    this();
    ErrorMessagesInterpolator interpolator =
        new ErrorMessagesInterpolator(errorState.getUngroupedErrors());
    this.ungroupedErrors.addAll(interpolator.interpolate());
    errorState.getGroupedErrors().stream().map(JaxbGroupedError::new)
        .forEach(this.groupedErrors::add);
  }

  @Override
  public Collection<ErrorMessage> getUngroupedErrors() {
    ErrorMessagesBuilder messagesBuilder = new ErrorMessagesBuilder(ungroupedErrors);
    return messagesBuilder.build();
  }

  @Override
  public Collection<GroupedError> getGroupedErrors() {
    return CollectionWrapper.of(groupedErrors).cast(GroupedError.class).normalize(ArrayList::new);
  }

  @ToString
  @XmlRootElement
  @NoArgsConstructor
  @EqualsAndHashCode
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class JaxbGroupedError implements GroupedError {
    @Getter
    @XmlElement
    private String groupName;

    @XmlElement
    private List<String> errorMessages = new ArrayList<>();

    public JaxbGroupedError(GroupedError groupedError) {
      this();
      this.groupName = groupedError.getGroupName();
      ErrorMessagesInterpolator interpolator =
          new ErrorMessagesInterpolator(groupedError.getMessages());
      this.errorMessages.addAll(interpolator.interpolate());
    }

    @Override
    public Collection<ErrorMessage> getMessages() {
      ErrorMessagesBuilder messagesBuilder = new ErrorMessagesBuilder(errorMessages);
      return messagesBuilder.build();
    }
  }

  @RequiredArgsConstructor
  private static class ErrorMessagesBuilder {
    private final Collection<String> errorMessages;

    public List<ErrorMessage> build() {
      return CollectionWrapper.of(errorMessages).map(ErrorMessage::createFor)
          .normalize(ArrayList::new);
    }
  }

  @RequiredArgsConstructor
  private static class ErrorMessagesInterpolator {
    private final Collection<ErrorMessage> errorMessages;

    public List<String> interpolate() {
      return CollectionWrapper.of(errorMessages).map(ErrorMessageInterpolator::new)
          .map(ErrorMessageInterpolator::interpolate).normalize(ArrayList::new);
    }
  }

  @RequiredArgsConstructor
  private static class ErrorMessageInterpolator {
    private final ErrorMessage errorMessage;

    public String interpolate() {
      return Text.interpolate(
          TextTemplates.createBy(errorMessage.getMessage(), errorMessage.getParameters()));
    }
  }
}
