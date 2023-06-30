package cloud.bangover.logging;

import cloud.bangover.text.TextTemplate;

interface LogMessageFactory {
  TextTemplate createLogMessageText();
}