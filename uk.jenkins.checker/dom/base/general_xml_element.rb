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
  def get_name
    return @element&.name
  end
  def get_text
    return @element&.text
  end
  def get_child_element(path)
    original = @element&.elements[path]
    return GeneralXmlElement.new(original)
  end

  def get_child_elements(path)
    if @element.nil?
      return []
    end

    return @element.elements[path].elements.map {|element| GeneralXmlElement.new(element)}
  end

end
