# frozen_string_literal: true
require 'rexml/document'

class BuildXml
  BUILD_XML_ROOT = 'build'.freeze
  def initialize(jobPath, number)
    @doc = read_buildXml(jobPath, number)
  end

  def get_parameter(parameterType, parameterName)
    parameter_value = nil

    get_param = -> (element)  {
      element.elements.each(parameterType) do | child |
        get_element_text = -> (grandchild) { grandchild.text }

        if child.get_element("name", get_element_text) == parameterName
          parameter_value = child.get_element("value", get_element_text)
        end
      end
    }

    get_element("#{BUILD_XML_ROOT}/actions/hudson.model.ParametersAction/parameters", get_param)
    return parameter_value
  end

  def get_result
    get_element_text = -> (element) { element.text }
    return get_element('build/result', get_element_text)
  end

  def get_element(path, parse_element)
    if(!@doc)
      return nil
    end

    return parse_element.call(@doc.elements[path])
  end

  private def get_build_dir_path(jobPath, number)
    return "#{jobPath}\\builds\\#{number}"
  end
  private def read_buildXml(jobPath, number)
    buildDir = get_build_dir_path(jobPath, number)
    fileName = "build.xml"

    if (!File.exist?("#{buildDir}\\#{fileName}"))
      return nil
    end

    Dir.chdir(buildDir)
    return REXML::Document.new(File.new(fileName))
  end
end
