# frozen_string_literal: true
require 'rexml/document'
class GeneralXmlElement
  def self.build_for_doc(path)
    if (!File.exist?(path))
      return GeneralXmlElement.new(nil)
    end

    doc = REXML::Document.new(File.new(path))
    return GeneralXmlElement.new(doc)
  end

  def initialize(element)
    @element = element
  end
  def get_text
    if(!@element)
      return nil
    end

    return @element.text
  end
  def get_child_element(path)
    if(!@element)
      return GeneralXmlElement.new(nil)
    end

    original = @element.elements[path]
    return GeneralXmlElement.new(original)
  end

end
