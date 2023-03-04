# frozen_string_literal: true
require 'rexml/document'

class BuildXml
  BUILD_XML_ROOT = 'build'.freeze
  def initialize(jobPath, number)
    @doc = read_buildXml(jobPath, number)
  end

  def get_parameter(parameterType, parameterName)
    parameter_value = nil
    parameters = @doc.elements["#{BUILD_XML_ROOT}/actions/hudson.model.ParametersAction/parameters"]

    parameters.elements.each(parameterType) do | element |
      if element.elements["name"].text == parameterName
        parameter_value = element.elements["value"].text
      end
    end

    return parameter_value
  end

  def get_result
    return @doc.elements['build/result'].text
  end

  private def get_build_dir_path(jobPath, number)
    return "#{jobPath}\\builds\\#{number}"
  end
  private def read_buildXml(jobPath, number)
    Dir.chdir(get_build_dir_path(jobPath, number))
    return REXML::Document.new(File.new("build.xml"))
  end
end
